package es.upv.grycap.tracer.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.time.Duration;
import java.util.Base64;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Hex;
import org.bitcoinj.core.Base58;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ripple.cryptoconditions.Ed25519Sha256Condition;
import com.ripple.cryptoconditions.Ed25519Sha256Fulfillment;
import com.ripple.cryptoconditions.der.DerOutputStream;

import es.upv.grycap.tracer.model.Trace;
import es.upv.grycap.tracer.model.dto.ReqDTO;
import es.upv.grycap.tracer.model.dto.bigchaindb.Asset;
import es.upv.grycap.tracer.model.dto.bigchaindb.AssetCreate;
import es.upv.grycap.tracer.model.dto.bigchaindb.Condition;
import es.upv.grycap.tracer.model.dto.bigchaindb.Input;
import es.upv.grycap.tracer.model.dto.bigchaindb.Output;
import es.upv.grycap.tracer.model.dto.bigchaindb.Subcondition;
import es.upv.grycap.tracer.model.dto.bigchaindb.SubconditionED25519;
import es.upv.grycap.tracer.model.dto.bigchaindb.Transaction;
import es.upv.grycap.tracer.model.dto.bigchaindb.Transaction.Operation;
import es.upv.grycap.tracer.model.exceptions.BigchaindbException;
import es.upv.grycap.tracer.model.exceptions.TransactionNotFoundException;
import es.upv.grycap.tracer.model.exceptions.UncheckedInterruptedException;
import es.upv.grycap.tracer.model.exceptions.UncheckedInvalidKeyException;
import es.upv.grycap.tracer.model.exceptions.UncheckedJsonMappingException;
import es.upv.grycap.tracer.model.exceptions.UncheckedJsonProcessingException;
import es.upv.grycap.tracer.model.exceptions.UncheckedNoSuchAlgorithmException;
import es.upv.grycap.tracer.model.exceptions.UncheckedSignatureException;
import es.upv.grycap.tracer.persistence.TransactionCacheRecRepository;
import lombok.extern.slf4j.Slf4j;
import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class BigchainDbManager implements BlockchainManager {
	
    private static final String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
    private static final BigInteger BIG0 = BigInteger.ZERO;
    private static final BigInteger BIG58 = BigInteger.valueOf(58);
    
    public static final String TRACE_USER_ID = "userId";
	
//	@Autowired
//	protected TransactionCacheRecRepository transactionCacheRecRepository;
	
	@Autowired
	protected NodeKeysManager nodeKeysManager;
	
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(20);
    //protected enum TransactionMode {sync, commit, async}
	
	protected WebClient wb;
	
	protected String transactionModePost;
	
	@Autowired
	protected HashingService hashingService;
	
	protected String blockchaindbBaseUrl;
	
	protected int defaultAmountTransaction;
	
	@Autowired
	public BigchainDbManager(@Value("${blockchaindb.baseUrl}") String blockchaindbBaseUrl,
			@Value("${blockchaindb.transactionModePost}") String transactionModePost,
			@Value("${blockchaindb.defaultAmountTransaction}") int defaultAmountTransaction) {
		this.blockchaindbBaseUrl = blockchaindbBaseUrl;
		this.transactionModePost = transactionModePost;
		this.defaultAmountTransaction = defaultAmountTransaction;
	}
	
	@PostConstruct
	protected void init() {
		wb = WebClient.create(blockchaindbBaseUrl);
	}
	
	@Override
	public void addEntry(final ReqDTO entry) {
		try {
			Transaction<?, ?, ?> tr = buildTransaction(entry);
			log.info(getObjectWriter().writeValueAsString(tr));
			HttpClient client = HttpClient.newHttpClient();
	        HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create(this.blockchaindbBaseUrl + "/transactions?mode=" + transactionModePost))
	                .POST(HttpRequest.BodyPublishers.ofString(getObjectWriter().writeValueAsString(tr)))
	                .build();
	        HttpResponse<String> response = client.send(request,
	                HttpResponse.BodyHandlers.ofString());
	        log.info(response.body());
		} catch (JsonProcessingException ex) {
			throw new UncheckedJsonProcessingException(ex);
		} catch (InvalidKeyException ex) {
			throw new UncheckedInvalidKeyException(ex);
		} catch (NoSuchAlgorithmException ex) {
			throw new UncheckedNoSuchAlgorithmException(ex);
		} catch (SignatureException ex) {
			throw new UncheckedSignatureException(ex);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		} catch (InterruptedException e) {
			throw new UncheckedInterruptedException(e);
		}
	}
	
	@Override
	public Transaction<?, ?, ?> getTransactionById(final String transactionId) {
		String transaction = wb.get().uri("/transactions/" + transactionId)
        .retrieve()
        .onStatus(httpStatus -> HttpStatus.ACCEPTED.equals(httpStatus), 
        		response -> Mono.empty())
        .onStatus(httpStatus -> HttpStatus.NOT_FOUND.equals(httpStatus), 
        		response -> Mono.error(new TransactionNotFoundException(response.bodyToMono(String.class).block(REQUEST_TIMEOUT))))
        .onStatus(httpStatus -> HttpStatus.BAD_REQUEST.equals(httpStatus), 
        		response -> Mono.error(new BigchaindbException(response.bodyToMono(String.class).block(REQUEST_TIMEOUT))))
        .bodyToMono(String.class)
        .block(REQUEST_TIMEOUT);
		try {
			return getObjectReader().readValue(transaction);
		} catch (JsonMappingException ex) {
			throw new UncheckedJsonMappingException(ex);
		} catch (JsonProcessingException ex) {
			throw new UncheckedJsonProcessingException(ex);
		}
	}
	
	public List<Trace> getTraceEntriesByUserId(final String userId) {
		String tracesJSON = getAssetsByField(Trace.FNAME_USER_ID, userId);
		try {
			return getObjectReader().readValue(tracesJSON);
		} catch (JsonMappingException ex) {
			throw new UncheckedJsonMappingException(ex);
		} catch (JsonProcessingException ex) {
			throw new UncheckedJsonProcessingException(ex);
		}
	}
	
	protected String getAssetsByField(String fieldName, String fieldValue) {
		String assets = wb.get().uri("/assets?search=" + 
				"\"" + fieldName + "\":\"" + fieldValue + "\"")
		        .retrieve()
		        .onStatus(httpStatus -> HttpStatus.ACCEPTED.equals(httpStatus), 
		        		response -> Mono.empty())
		        .onStatus(httpStatus -> HttpStatus.NOT_FOUND.equals(httpStatus), 
		        		response -> Mono.error(new TransactionNotFoundException(response.bodyToMono(String.class).block(REQUEST_TIMEOUT))))
		        .onStatus(httpStatus -> HttpStatus.BAD_REQUEST.equals(httpStatus), 
		        		response -> Mono.error(new BigchaindbException(response.bodyToMono(String.class).block(REQUEST_TIMEOUT))))
		        .bodyToMono(String.class)
		        .block(REQUEST_TIMEOUT);
				return assets;
	}
	
	protected Transaction<?, ?, ?> buildTransaction(final ReqDTO entry) 
			throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		Asset asset = buildAsset(entry);
		Ed25519Sha256Fulfillment fulfillment = generateFulfillment();
		List<Input> inputs = buildInputs(entry, fulfillment);
		List<Output> outputs = buildOutputs(entry, defaultAmountTransaction, fulfillment);
		Transaction<?,?, ?> tr = Transaction.builder()
				.asset(asset)
				.id(null)
				.inputs(inputs)
				.metadata(null)
				.operation(Operation.CREATE)
				.outputs(List.of())//outputs)
				.build();
		sign(tr);
		String id = determineId(tr);
		tr.setId(id);
		return tr;
	}
	
    private void sign(Transaction<?,?, ?> tr)
            throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, JsonProcessingException {
    	byte[] jsonRepr = getObjectWriter().writeValueAsBytes(tr);

        byte[] sha3Hash = hashingService.getHash(jsonRepr);

        // signing the transaction
        Signature edDsaSigner = new EdDSAEngine(MessageDigest.getInstance("SHA-512"));
        edDsaSigner.initSign(nodeKeysManager.getKeyPair().getPrivate());
        edDsaSigner.update(sha3Hash);
        byte[] signature = edDsaSigner.sign();
        //Ed25519Sha256Fulfillment fulfillment = Ed25519Sha256Fulfillment.from(
		//		  (EdDSAPublicKey) nodeKeysManager.getKeyPair().getPublic(), signature);
        tr.getInputs().get(0)
                .setFulfillment(Base64.getUrlEncoder().encodeToString(getEncoded(signature)));
    }
    
    public byte[] getEncoded(byte[] signature)
    {
      try
      {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DerOutputStream out = new DerOutputStream(baos);
        out.writeTaggedObject(0, ((EdDSAPublicKey) nodeKeysManager.getKeyPair().getPublic()).getA().toByteArray());
        out.writeTaggedObject(1, signature);
        out.close();
        byte[] buffer = baos.toByteArray();
        

        baos = new ByteArrayOutputStream();
        out = new DerOutputStream(baos);
        out.writeTaggedConstructedObject(4, buffer);
        out.close();
        
        return baos.toByteArray();
      }
      catch (IOException e) {
        throw new RuntimeException("DER Encoding Error", e);
      }
    }
	
	protected Asset buildAsset(final ReqDTO entry) {
		AssetCreate<Trace> asset = new AssetCreate<>();
		asset.setData(Trace.builder()
				.datasetId(entry.getDatasetId())
				.type(entry.getUserAction())
				.userId(entry.getUserId())
				.build()
				);
		return asset;
	}
	
	protected Ed25519Sha256Fulfillment generateFulfillment() 
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		MessageDigest sha512Digest = MessageDigest.getInstance("SHA-512");
		  Signature edDsaSigner = new EdDSAEngine(sha512Digest);

		  log.info("Digets of private key: " + ((EdDSAPrivateKey) nodeKeysManager.getKeyPair().getPrivate()).getParams().getHashAlgorithm());
		  edDsaSigner.initSign(nodeKeysManager.getKeyPair().getPrivate());
		  //edDsaSigner.update(new byte[0]);
		  byte[] edDsaSignature = edDsaSigner.sign();

		  //Generate ED25519-SHA-256 Fulfillment and Condition
		  Ed25519Sha256Fulfillment fulfillment = Ed25519Sha256Fulfillment.from(
				  (EdDSAPublicKey) nodeKeysManager.getKeyPair().getPublic(), edDsaSignature);
		  return fulfillment;
	}
	
	protected List<Input> buildInputs(final ReqDTO entry, Ed25519Sha256Fulfillment fulfillment) 
			throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
//		String ownerPublicK = new String(Base64.getDecoder().decode(entry.getUserPublicKey()));
//		  byte[] optionalMessageToSign = new byte[0];
//
//		  //Generate ED25519-SHA-256 KeyPair and Signer
//		  MessageDigest sha512Digest = MessageDigest.getInstance("SHA3-512");
//		  net.i2p.crypto.eddsa.KeyPairGenerator edDsaKpg = new net.i2p.crypto.eddsa.KeyPairGenerator();
//		  KeyPair edDsaKeyPair = edDsaKpg.generateKeyPair();

		  //ByteArrayInputStream inStream = new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(fulfillment));
		    //ASN1InputStream asnInputStream = new ASN1InputStream(inStream);

		  Ed25519Sha256Condition condition = fulfillment.getDerivedCondition();
		return List.of(Input.builder().fulfills(null)
				.ownersBefore(List.of(
						Base58.encode(nodeKeysManager.getKeyPair().getPublic().getEncoded()) //Base64.getDecoder().decode(entry.getUserPublicKey()))
						))
				.fulfillment(null)//getSignatureBase64Url())//Base64.getUrlEncoder().encodeToString(bytes))
				.build());
	}
	
	protected List<Output> buildOutputs(final ReqDTO entry, int amount, Ed25519Sha256Fulfillment fulfillment) {
//		final Ed25519Sha256Condition ed25519cond = 
//				Ed25519Sha256Condition.from((EdDSAPublicKey) nodeKeysManager.getKeyPair().getPublic());
		final Subcondition sc = new SubconditionED25519(Base58.encode(nodeKeysManager.getKeyPair().getPublic().getEncoded()));//Base64.getDecoder().decode(entry.getUserPublicKey())));
		final String uri = new StringBuilder().append("ni:///sha-256;")
				.append(fulfillment.getDerivedCondition().getFingerprintBase64Url())//ed25519cond.getFingerprintBase64Url())
				.append("?fpt=")
				.append(sc.getType().getValue())
				.append("&cost=")
				.append(sc.getType().getCost())
				.toString();
		Condition cond = Condition.builder().details(sc).uri(uri).build();
		
		return List.of(
				Output.builder()
					.amount(Integer.toString(amount)).condition(cond)
					.publicKeys(List.of(
							Base58.encode(nodeKeysManager.getKeyPair().getPublic().getEncoded())//Base64.getDecoder().decode(entry.getUserPublicKey()))
							))
					.build()
				);
	}
	
	protected String determineId(final Transaction<?, ?, ?> tr) throws JsonProcessingException, NoSuchAlgorithmException {
		byte[] jsonRepr = getObjectWriter().writeValueAsBytes(tr);
		return Hex.encodeHexString(hashingService.getHash(jsonRepr));
	}
	
	protected ObjectReader getObjectReader() {
		return new ObjectMapper().reader();
	}
	
	protected ObjectWriter getObjectWriter() {
		return new ObjectMapper().configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true).writer();
	}
//
//    protected static String convertToBase58(String hash) {
//        return convertToBase58(hash, 16);
//    }
//	
//    protected static String convertToBase58(String hash, int base) {
//        BigInteger x;
//        if (base == 16 && hash.substring(0, 2).equals("0x")) {
//            x = new BigInteger(hash.substring(2), 16);
//        } else {
//            x = new BigInteger(hash, base);
//        }
// 
//        StringBuilder sb = new StringBuilder();
//        while (x.compareTo(BIG0) > 0) {
//            int r = x.mod(BIG58).intValue();
//            sb.append(ALPHABET.charAt(r));
//            x = x.divide(BIG58);
//        }
// 
//        return sb.reverse().toString();
//    }

}
