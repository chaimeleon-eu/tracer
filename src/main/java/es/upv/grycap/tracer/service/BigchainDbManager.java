package es.upv.grycap.tracer.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigInteger;
import java.net.ConnectException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.channels.ClosedChannelException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.util.Strings;
import org.bitcoinj.core.Base58;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ripple.cryptoconditions.Ed25519Sha256Condition;
import com.ripple.cryptoconditions.Ed25519Sha256Fulfillment;
import com.ripple.cryptoconditions.der.DerOutputStream;

import es.upv.grycap.tracer.Util;
import es.upv.grycap.tracer.exceptions.UncheckedKeyManagementException;
import es.upv.grycap.tracer.model.BigchainDbProperties;
import es.upv.grycap.tracer.model.BlockchainProperties;
import es.upv.grycap.tracer.model.FilterParams;
import es.upv.grycap.tracer.model.IFilterParams;
import es.upv.grycap.tracer.model.TraceCacheOpResult;
import es.upv.grycap.tracer.model.dto.BlockchainType;
import es.upv.grycap.tracer.model.dto.HashType;
import es.upv.grycap.tracer.model.dto.ITransaction;
import es.upv.grycap.tracer.model.dto.ReqCacheStatus;
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
import es.upv.grycap.tracer.exceptions.BigchaindbException;
import es.upv.grycap.tracer.exceptions.TraceException;
import es.upv.grycap.tracer.exceptions.TransactionNotFoundException;
import es.upv.grycap.tracer.exceptions.UncheckedExceptionFactory;
import es.upv.grycap.tracer.exceptions.UncheckedInterruptedException;
import es.upv.grycap.tracer.exceptions.UncheckedInvalidKeyException;
import es.upv.grycap.tracer.exceptions.UncheckedJsonMappingException;
import es.upv.grycap.tracer.exceptions.UncheckedJsonProcessingException;
import es.upv.grycap.tracer.exceptions.UncheckedNoSuchAlgorithmException;
import es.upv.grycap.tracer.exceptions.UncheckedSignatureException;
import es.upv.grycap.tracer.model.trace.TraceBase;
import es.upv.grycap.tracer.model.trace.TraceSummaryBase;
import es.upv.grycap.tracer.model.trace.TraceVersion;
import es.upv.grycap.tracer.model.trace.v1.Trace;
import lombok.extern.slf4j.Slf4j;
import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;

@Service
@Slf4j
public class BigchainDbManager implements BlockchainManager {


    public static final String TRACE_USER_ID = "userId";

	//@Autowired
	protected NodeKeysManager nodeKeysManager;

	@Autowired
	protected TraceHandler traceHandler;

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(20);
    //protected enum TransactionMode {sync, commit, async}

//	protected WebClient wb;

//	protected String transactionModePost;

	
	//protected HashingService hashingService;
	protected TraceFiltering traceFiltering;

//	protected String blockchaindbBaseUrl;

//	protected int defaultAmountTransaction;
	
	protected boolean disableSSLVerification;
	
	protected final BigchainDbProperties props;

	@Autowired
	public BigchainDbManager(@Autowired final BigchainDbProperties props,
			@Value("${tracer.disableSSLVerification}") boolean disableSSLVerification,
			//@Autowired HashingService hashingService,
			@Autowired TraceFiltering traceFiltering) 
					throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, IOException {
		this.props = props;
//		this.blockchaindbBaseUrl = blockchaindbBaseUrl;
//		this.transactionModePost = transactionModePost;
//		this.defaultAmountTransaction = defaultAmountTransaction;
		this.disableSSLVerification = disableSSLVerification;
		log.info("Disable SSL verification: " + disableSSLVerification);
		//this.hashingService = hashingService;
		this.traceFiltering = traceFiltering;
		nodeKeysManager = new NodeKeysManager(props.getKeypairPrivate(), props.getKeypairPublic());
		nodeKeysManager.init();
	}

//	@PostConstruct
//	protected void init() {
//		wb = WebClient.create(blockchaindbBaseUrl);
//	}
	
	public ITransaction<?> generateTransaction(final TraceBase trace, String callerUserId) {
		//final TraceBase trace = traceHandler.fromRequest(entry, callerUserId);
		try {
			return buildTransaction(trace);
		} catch (JsonProcessingException | InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
			log.error(Util.getFullStackTrace(e));
			throw UncheckedExceptionFactory.get(e);
		}
	}

	@Override
	public TraceCacheOpResult submitTransaction(final ITransaction<?> transaction) {
		String resultMsg = null;
		ReqCacheStatus resultStatus = null;
		String tId = null;
		try {
			
			tId = transaction.getId();
//			traceCacheRepository.saveAndFlush(TraceCacheEntry.builder()
//					.idTransaction(tr.getId())
//					.submitDate(Instant.now())
//					.status(TraceCacheEntry.Status.SUBMITTED)
//					.trace(trace)
//					.build());

			//log.info(getObjectWriter().writeValueAsString(tr));
			HttpClient client = Util.getHttpClient(disableSSLVerification);
	        HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create(props.getUrl() + "/transactions?"
	                		//+ "mode=commit"))
	                		+ props.getTransactionModePost().name()))
	                .POST(HttpRequest.BodyPublishers.ofString(getObjectWriter().writeValueAsString(transaction)))
	                .build();
	        HttpResponse<String> response = client.send(request,
	                HttpResponse.BodyHandlers.ofString());
	        if (response.statusCode() > 299) {
	        	//throw new BigchaindbException(response.body());
				resultMsg = "Blockchain returned status " + response.statusCode() + " with the following message: " + response.body();
				resultStatus = ReqCacheStatus.BLOCKCHAIN_ERROR;
	        } else {
	        	resultStatus = ReqCacheStatus.BLOCKCHAIN_SUCCESS;
	        }
	        //log.info(response.body());
		} catch (ConnectException | ClosedChannelException e) {
			// Blockchain is not available
			String st = Util.getFullStackTrace(e);
			log.error(st);
			resultMsg = st;
			resultStatus = ReqCacheStatus.BLOCKCHAIN_UNAVAILABLE;
		} catch (Exception e) {
			log.error(Util.getFullStackTrace(e));
			resultMsg = Util.getFullStackTrace(e);
			resultStatus = ReqCacheStatus.ERROR;
		}
//		} catch (JsonProcessingException ex) {
//			log.error(ex.getMessage(), ex);
//			
//			//throw new UncheckedJsonProcessingException(ex);
//		} catch (InvalidKeyException ex) {
//			log.error(ex.getMessage(), ex);
//			//throw new UncheckedInvalidKeyException(ex);
//		} catch (NoSuchAlgorithmException ex) {
//			log.error(ex.getMessage(), ex);
//			//throw new UncheckedNoSuchAlgorithmException(ex);
//		} catch (SignatureException ex) {
//			log.error(ex.getMessage(), ex);
//			//throw new UncheckedSignatureException(ex);
//		} catch (IOException ex) {
//			log.error(ex.getMessage(), ex);
//			//throw new UncheckedIOException(ex);
//		} catch (InterruptedException ex) {
//			log.error(ex.getMessage(), ex);
//			//throw new UncheckedInterruptedException(ex);
//		} catch (KeyManagementException ex) {
//			log.error(ex.getMessage(), ex);
//			//throw new UncheckedKeyManagementException(ex);
//		}
		return new TraceCacheOpResult(resultMsg, resultStatus, tId);
	}
	
	@Override
	public TraceCacheOpResult getTransactionStatusById(final String transactionId) {
		String resultMsg = null;
		ReqCacheStatus resultStatus = null;
		try {
//			traceCacheRepository.saveAndFlush(TraceCacheEntry.builder()
//					.idTransaction(tr.getId())
//					.submitDate(Instant.now())
//					.status(TraceCacheEntry.Status.SUBMITTED)
//					.trace(trace)
//					.build());

			//log.info(getObjectWriter().writeValueAsString(tr));
			HttpClient client = Util.getHttpClient(disableSSLVerification);
	        HttpRequest request = HttpRequest.newBuilder()
	        		.uri(URI.create(props.getUrl() + "/transactions/" + transactionId)).GET().build();
	        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
	        if (response.statusCode() > 299) {
	        	//throw new BigchaindbException(response.body());
				resultMsg = "Blockchain returned status " + response.statusCode() + " with the following message: " + response.body();
				resultStatus = ReqCacheStatus.BLOCKCHAIN_ERROR;
	        } else {
	        	resultStatus = ReqCacheStatus.BLOCKCHAIN_SUCCESS;
	        }
	        //log.info(response.body());
		} catch (ConnectException | ClosedChannelException e) {
			// Blockchain is not available
			String st = Util.getFullStackTrace(e);
			log.error(st);
			resultMsg = st;
			resultStatus = ReqCacheStatus.BLOCKCHAIN_UNAVAILABLE;
		}  catch (Exception e) {
			log.error(Util.getFullStackTrace(e));
			resultMsg = Util.getFullStackTrace(e);
			resultStatus = ReqCacheStatus.ERROR;
		}
		return new TraceCacheOpResult(resultMsg, resultStatus, transactionId);
		
	}

//	@Override
//	public ITransaction getTransactionById(final String transactionId) {
//		String resultMsg = null;
//		ReqCacheStatus resultStatus = null;
//		try {
////			traceCacheRepository.saveAndFlush(TraceCacheEntry.builder()
////					.idTransaction(tr.getId())
////					.submitDate(Instant.now())
////					.status(TraceCacheEntry.Status.SUBMITTED)
////					.trace(trace)
////					.build());
//
//			//log.info(getObjectWriter().writeValueAsString(tr));
//			HttpClient client = Util.getHttpClient(disableSSLVerification);
//	        HttpRequest request = HttpRequest.newBuilder()
//	                .uri(URI.create(props.getUrl() + "/transactions/" + transactionId)).GET().build();
//	        HttpResponse<String> response = client.send(request,
//	                HttpResponse.BodyHandlers.ofString());
//	        if (response.statusCode() > 299) {
//	        	return null;
//	        } else {
//	        	return getObjectReader().readValue(response.body(), Transaction.class);
//	        }
//	        //log.info(response.body());
//		} catch (Exception e) {
//			log.error(ExceptionUtils.getStackTrace(e));
//			resultMsg = ExceptionUtils.getStackTrace(e);
//			resultStatus = ReqCacheStatus.ERROR;
//		}
//	}
	@Override
	public List<TraceSummaryBase> getTraces(final FilterParams fp) {

		try {
			HttpResponse<String> response = null;
			Collection<String> vals = fp.toCollectionOfValues();
			if (vals.isEmpty()) {
				response = getAssetsByTraceVersion(List.of(TraceVersion.V1.name()));
			} else {
				response = getAssetsByFieldsValues(vals);
			}
			
	        log.info(response.toString());
	        ObjectMapper mapper = new ObjectMapper();
	        List<AssetCreate<TraceBase>> assets = mapper.readValue(response.body(), new TypeReference<List<AssetCreate<TraceBase>>>(){});
	        //List<AssetCreate<Trace>> assets = getObjectReader().forType(new TypeReference<List<AssetCreate<Trace>>>(){}).<AssetCreate<Trace>>readValues(response.body()).readAll();
			List<TraceBase>  traces = assets.stream().filter(asset -> asset instanceof AssetCreate).map(asset -> ((AssetCreate<TraceBase>) asset).getData())
					.collect(Collectors.toList());
			 return traces.stream().map(e -> e.toSummary()).toList();//traceFiltering.filterTraces(traces, fp);
		} catch (JsonProcessingException ex) {
			log.error(ExceptionUtils.getStackTrace(ex));
			throw new UncheckedJsonProcessingException(ex);
		} catch (IOException ex) {
			log.error(ExceptionUtils.getStackTrace(ex));
			throw new UncheckedIOException(ex);
		} catch (InterruptedException ex) {
			log.error(ExceptionUtils.getStackTrace(ex));
			throw new UncheckedInterruptedException(ex);
		} catch (KeyManagementException ex) {
			log.error(ExceptionUtils.getStackTrace(ex));
			throw new UncheckedKeyManagementException(ex);
		} catch (NoSuchAlgorithmException ex) {
			log.error(ExceptionUtils.getStackTrace(ex));
			throw new UncheckedNoSuchAlgorithmException(ex);
		}
	}
	
	@Override
	public TraceBase getTraceById(String traceId) {
		try {
			HttpResponse<String> response = null;
			response = getAssetsByFieldsValues(List.of(traceId));
			
	        log.info(response.toString());
	        ObjectMapper mapper = new ObjectMapper();
	        List<AssetCreate<TraceBase>> assets = mapper.readValue(response.body(), new TypeReference<List<AssetCreate<TraceBase>>>(){});
	        //List<AssetCreate<Trace>> assets = getObjectReader().forType(new TypeReference<List<AssetCreate<Trace>>>(){}).<AssetCreate<Trace>>readValues(response.body()).readAll();
			List<TraceBase>  traces = assets.stream().filter(asset -> asset instanceof AssetCreate).map(asset -> ((AssetCreate<TraceBase>) asset).getData())
					.collect(Collectors.toList());
			 List<TraceBase> filt = traces.stream().filter(e -> e.getId().contentEquals(traceId)).toList();
			 if (filt.size() > 1) {
				 throw new TraceException("Too many traces (" + filt.size() + ") for ID " + traceId);
			 } else if (filt.size() == 1) {
				 return filt.get(0);
			 } else {
				 return null;
			 }
			 //traceFiltering.filterTraces(traces, fp);
		} catch (JsonProcessingException ex) {
			log.error(ExceptionUtils.getStackTrace(ex));
			throw new UncheckedJsonProcessingException(ex);
		} catch (IOException ex) {
			log.error(ExceptionUtils.getStackTrace(ex));
			throw new UncheckedIOException(ex);
		} catch (InterruptedException ex) {
			log.error(ExceptionUtils.getStackTrace(ex));
			throw new UncheckedInterruptedException(ex);
		} catch (KeyManagementException ex) {
			log.error(ExceptionUtils.getStackTrace(ex));
			throw new UncheckedKeyManagementException(ex);
		} catch (NoSuchAlgorithmException ex) {
			log.error(ExceptionUtils.getStackTrace(ex));
			throw new UncheckedNoSuchAlgorithmException(ex);
		}
	}
	
	protected HttpResponse<String> getAssetsByTraceVersion(final List<String> versions)
			throws IOException, InterruptedException, KeyManagementException, NoSuchAlgorithmException {
		HttpClient client = Util.getHttpClient(disableSSLVerification);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(props.getUrl() + 
                		"/assets?search=" + 
                			URLEncoder.encode("\"" + Strings.join(versions, ',') + "\"", StandardCharsets.UTF_8)))
                .GET()
                .build();
         return client.send(request,
                HttpResponse.BodyHandlers.ofString());
	}

	protected HttpResponse<String> getAssetsByFieldsValues(Collection<String> fieldsValues)
			throws IOException, InterruptedException, KeyManagementException, NoSuchAlgorithmException {
		HttpClient client = Util.getHttpClient(disableSSLVerification);
		String searchParams = fieldsValues.stream().map(fv -> URLEncoder.encode("\"" + fv + "\"", StandardCharsets.UTF_8))
        		.collect(Collectors.joining(" "));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(props.getUrl() + "/assets?search=" + searchParams))
                .GET()
                .build();
         return client.send(request,
                HttpResponse.BodyHandlers.ofString());
	}

	protected Transaction<?, ?, ?> buildTransaction(final TraceBase trace)
			throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		final Asset asset = buildAsset(trace);
		Ed25519Sha256Fulfillment fulfillment = generateFulfillment();
		List<Input> inputs = buildInputs(fulfillment);
		List<Output> outputs = buildOutputs(props.getDefaultAmountTransaction(), fulfillment);
		Transaction<?,?, ?> tr = Transaction.builder()
				.asset(asset)
				.id(null)
				.inputs(inputs)
				.metadata(null)
				.operation(Operation.CREATE)
				.outputs(outputs)
				.build();
		sign(tr);
		String id = determineId(tr);
		tr.setId(id);
		return tr;
	}

    private void sign(Transaction<?,?, ?> tr)
            throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, JsonProcessingException {
    	byte[] jsonRepr = getObjectWriter().writeValueAsBytes(tr);

        byte[] sha3Hash = HashingService.getHash(jsonRepr, HashType.SHA3_256).getHash();

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

	protected Asset buildAsset(final TraceBase trace) {
		AssetCreate<TraceBase> asset = new AssetCreate<>();
		asset.setData(trace);
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

	protected List<Input> buildInputs(Ed25519Sha256Fulfillment fulfillment)
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

	protected List<Output> buildOutputs(int amount, Ed25519Sha256Fulfillment fulfillment) {
//		final Ed25519Sha256Condition ed25519cond =
//				Ed25519Sha256Condition.from((EdDSAPublicKey) nodeKeysManager.getKeyPair().getPublic());
		final Subcondition sc = new SubconditionED25519(Base58.encode(
				((EdDSAPublicKey) nodeKeysManager.getKeyPair().getPublic()).getA().toByteArray()));//Base64.getDecoder().decode(entry.getUserPublicKey())));
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
							Base58.encode(((EdDSAPublicKey) nodeKeysManager.getKeyPair().getPublic()).getA().toByteArray())//Base64.getDecoder().decode(entry.getUserPublicKey()))
							))
					.build()
				);
	}

	protected String determineId(final Transaction<?, ?, ?> tr) throws JsonProcessingException, NoSuchAlgorithmException {
		byte[] jsonRepr = getObjectWriter().writeValueAsBytes(tr);
		return Hex.encodeHexString(HashingService.getHash(jsonRepr, HashType.SHA3_256).getHash());
	}

	protected ObjectReader getObjectReader() {
		return new ObjectMapper().reader();
	}

	protected ObjectWriter getObjectWriter() {
		return new ObjectMapper().configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true).writer();
	}

	@Override
	public BlockchainProperties getBlockchainProperties() {
		return props;
	}

	@Override
	public BlockchainType getType() {
		return BlockchainType.BIGCHAINDB_PRIVATE;
	}

}
