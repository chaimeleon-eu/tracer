package es.upv.grycap.tracer.service;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Hex;
import org.bitcoinj.core.Base58;
import org.bouncycastle.jcajce.spec.EdDSAParameterSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ripple.cryptoconditions.Ed25519Sha256Condition;
import com.ripple.cryptoconditions.Ed25519Sha256Fulfillment;

import es.upv.grycap.tracer.model.TraceEntry;
import es.upv.grycap.tracer.model.dto.EntryReqDTO;
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
import es.upv.grycap.tracer.persistence.TransactionCacheRecRepository;
import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;
import reactor.core.publisher.Mono;

@Service
public class BigchainDbManager implements BlockchainManager {
	
    private static final String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
    private static final BigInteger BIG0 = BigInteger.ZERO;
    private static final BigInteger BIG58 = BigInteger.valueOf(58);
	
	@Autowired
	protected TransactionCacheRecRepository transactionCacheRecRepository;
	
	@Autowired
	protected NodeKeysManager nodeKeysManager;
	
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(20);
    //protected enum TransactionMode {sync, commit, async}
	
	protected WebClient wb;
	
	@Value("${blockchaindb.transactionModePost}") 
	protected String transactionModePost;
	
	@Autowired
	protected HashingService hashingService;
	
	@PostConstruct
	protected void init(@Value("${tracer.blockchaindbBaseUrl}") String blockchaindbBaseUrl) {
		wb = WebClient.create(blockchaindbBaseUrl);
	}
	
	@Override
	public void addEntry(EntryReqDTO entry) {
		wb.post().uri("/transactions?mode=" + transactionModePost)
	        .retrieve()
	        .onStatus(httpStatus -> HttpStatus.ACCEPTED.equals(httpStatus), 
	        		response -> Mono.empty())
	        .onStatus(httpStatus -> HttpStatus.BAD_REQUEST.equals(httpStatus), 
	        		response -> Mono.error(new BigchaindbException(response.bodyToMono(String.class).block(REQUEST_TIMEOUT))))
	        .bodyToMono(String.class)
	        .block(REQUEST_TIMEOUT);
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
		return null;
	}
	
	protected Transaction<?, ?, ?> buildTransaction(final EntryReqDTO entry) throws JsonProcessingException, NoSuchAlgorithmException {
		Asset asset = buildAsset(entry);
		List<Input> inputs = null;//buildInputs(entry);
		List<Output> outputs = null;//buildOutputs(entry);
		Transaction<?,?, ?> tr = Transaction.builder()
				.asset(asset)
				.id(null)
				.inputs(inputs)
				.metadata(null)
				.operation(Operation.CREATE)
				.outputs(null)
				.build();
		String id = determineId(tr);
		tr.setId(id);
		return tr;
	}
	
	protected Asset buildAsset(final EntryReqDTO entry) {
		AssetCreate<TraceEntry> asset = new AssetCreate<>();
		asset.setData(TraceEntry.builder()
				.datasetId(entry.getDatasetId())
				.type(entry.getUserAction())
				.userId(entry.getUserId())
				.build()
				);
		return asset;
	}
	
	protected List<Input> buildInputs(final EntryReqDTO entry) 
			throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
//		String ownerPublicK = new String(Base64.getDecoder().decode(entry.getUserPublicKey()));
//		  byte[] optionalMessageToSign = new byte[0];
//
//		  //Generate ED25519-SHA-256 KeyPair and Signer
//		  MessageDigest sha512Digest = MessageDigest.getInstance("SHA3-512");
//		  net.i2p.crypto.eddsa.KeyPairGenerator edDsaKpg = new net.i2p.crypto.eddsa.KeyPairGenerator();
//		  KeyPair edDsaKeyPair = edDsaKpg.generateKeyPair();
		MessageDigest sha512Digest = MessageDigest.getInstance("SHA3-512");
		  Signature edDsaSigner = new EdDSAEngine(sha512Digest);

		  edDsaSigner.initSign(nodeKeysManager.getKeyPair().getPrivate());
		  edDsaSigner.update(new byte[0]);
		  byte[] edDsaSignature = edDsaSigner.sign();

		  //Generate ED25519-SHA-256 Fulfillment and Condition
		  Ed25519Sha256Fulfillment fulfillment = Ed25519Sha256Fulfillment.from(
				  (EdDSAPublicKey) nodeKeysManager.getKeyPair().getPublic(), edDsaSignature);
		  Ed25519Sha256Condition condition = fulfillment.getDerivedCondition();
		  fulfillment.toString();
		  //ByteArrayInputStream inStream = new ByteArrayInputStream(new ObjectMapper().writeValueAsBytes(fulfillment));
		    //ASN1InputStream asnInputStream = new ASN1InputStream(inStream);
		return List.of(Input.builder().fulfills(null)
				.ownersBefore(List.of(
						Base58.encode(nodeKeysManager.getKeyPair().getPublic().getEncoded()) //Base64.getDecoder().decode(entry.getUserPublicKey()))
						))
				.fulfillment(fulfillment.getSignatureBase64Url())//Base64.getUrlEncoder().encodeToString(bytes))
				.build());
	}
	
	protected List<Output> buildOutputs(final EntryReqDTO entry, int amount) {
		EdDSAPublicKeySpec pubKeySpec = new EdDSAPublicKeySpec(nodeKeysManager.getKeyPair().getPublic().getEncoded(),//Base64.getDecoder().decode(entry.getUserPublicKey()), 
				EdDSANamedCurveTable.getByName("Ed25519"));
		EdDSAPublicKey pubKey = new EdDSAPublicKey(pubKeySpec);
		Ed25519Sha256Condition ed25519cond =  Ed25519Sha256Condition.from(pubKey);
		Subcondition sc = new SubconditionED25519(Base58.encode(nodeKeysManager.getKeyPair().getPublic().getEncoded()));//Base64.getDecoder().decode(entry.getUserPublicKey())));
		String uri = new StringBuilder().append("ni:///sha-256;")
				.append(ed25519cond.getFingerprintBase64Url())
				.append("?fpt=")
				.append(sc.getType().getValue())
				.append("&cost=")
				.append(sc.getType().getCost())
				.toString();
		Condition cond = Condition.builder().details(sc).uri(uri).build();
		
		return List.of(
				Output.builder()
					.amount(amount).condition(cond)
					.publicKeys(List.of(
							Base58.encode(nodeKeysManager.getKeyPair().getPublic().getEncoded())//Base64.getDecoder().decode(entry.getUserPublicKey()))
							))
					.build()
				);
	}
	
	protected String determineId(final Transaction<?, ?, ?> tr) throws JsonProcessingException, NoSuchAlgorithmException {
		byte[] jsonRepr = new ObjectMapper().writeValueAsBytes(tr);
		return Hex.encodeHexString(hashingService.getHash(jsonRepr));
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
