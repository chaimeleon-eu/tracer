package es.upv.grycap.tracer.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Service;

import es.upv.grycap.tracer.model.DataHash;
import es.upv.grycap.tracer.model.dto.ReqResHashDTO;
import es.upv.grycap.tracer.model.dto.HashType;
import es.upv.grycap.tracer.model.dto.ReqResDTO;
import es.upv.grycap.tracer.model.dto.ReqResFileDataDTO;
import es.upv.grycap.tracer.model.dto.ReqResHttpDTO;
import es.upv.grycap.tracer.exceptions.UncheckedMalformedURLException;
import es.upv.grycap.tracer.exceptions.UncheckedNoSuchAlgorithmException;
import es.upv.grycap.tracer.exceptions.UnknownReqResType;

@Service
public class HashingService {
	
	public DataHash getHashReqResource(final ReqResDTO reqResource, HashType hashType) {
		if (reqResource instanceof ReqResFileDataDTO)
			return getHashReqResourceImpl((ReqResFileDataDTO)reqResource, hashType);
		else if (reqResource instanceof ReqResHttpDTO) 
			return getHashReqResourceImpl((ReqResHttpDTO)reqResource, hashType);
		else if (reqResource instanceof ReqResHashDTO) 
			return getHashReqResourceImpl((ReqResHashDTO)reqResource);
		else
			throw new UnknownReqResType("Unknown request resource type " + reqResource.getContentType());
	}
	
	protected DataHash getHashReqResourceImpl(final ReqResFileDataDTO reqResource, HashType hashType)  {
		byte[] fData = Base64.decode(reqResource.getData());
		return getHash(fData, hashType);
	}
	
	protected DataHash getHashReqResourceImpl(final ReqResHttpDTO reqResource, HashType hashType) {
		BufferedInputStream btc;
		try {
			btc = new BufferedInputStream(new URL(reqResource.getUrl()).openStream());
	        ByteArrayOutputStream btos = new ByteArrayOutputStream();
			 byte[] bucket = new byte[2048];
			    int numBytesRead;

			    while ((numBytesRead = btc.read(bucket, 0, bucket.length)) != -1) {
			    	btos.write(bucket, 0, numBytesRead);
			    }
			return getHash(btos.toByteArray(), hashType);
		} catch (MalformedURLException ex) {
			throw new UncheckedMalformedURLException(ex);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}
	
	protected DataHash getHashReqResourceImpl(final ReqResHashDTO reqResource) {
		byte[] fData = Base64.decode(reqResource.getHash());
		return DataHash.builder().hash(fData).originalData(null).hashType(reqResource.getHashType()).build();
	}
	
	public DataHash getHashFile(Path file, HashType hashType) {
		try {
			return getHash(Files.readAllBytes(file), hashType); 
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}
	
	public DataHash getHash(byte[] content, HashType hashType) {
		try {
			final MessageDigest digest = MessageDigest.getInstance(hashType.algorithmId);//"SHA3-256");
			return DataHash.builder().hash(digest.digest(content)).originalData(content).hashType(hashType).build();
		} catch (NoSuchAlgorithmException ex) {
			throw new UncheckedNoSuchAlgorithmException(ex);
		}
	}

}
