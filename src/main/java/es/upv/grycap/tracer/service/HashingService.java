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

import es.upv.grycap.tracer.model.dto.ReqResChecksumDTO;
import es.upv.grycap.tracer.model.dto.ReqResDTO;
import es.upv.grycap.tracer.model.dto.ReqResFileDataDTO;
import es.upv.grycap.tracer.model.dto.ReqResHttpDTO;
import es.upv.grycap.tracer.model.exceptions.UncheckedMalformedURLException;
import es.upv.grycap.tracer.model.exceptions.UncheckedNoSuchAlgorithmException;
import es.upv.grycap.tracer.model.exceptions.UnknownReqResType;

@Service
public class HashingService {
	
	public byte[] getHashReqResource(final ReqResDTO reqResource) {
		if (reqResource instanceof ReqResFileDataDTO)
			return getHashReqResourceImpl((ReqResFileDataDTO)reqResource);
		else if (reqResource instanceof ReqResHttpDTO) 
			return getHashReqResourceImpl((ReqResHttpDTO)reqResource);
		else if (reqResource instanceof ReqResChecksumDTO) 
			return getHashReqResourceImpl((ReqResChecksumDTO)reqResource);
		else
			throw new UnknownReqResType("Unknown request resource type " + reqResource.getType());
	}
	
	protected byte[] getHashReqResourceImpl(final ReqResFileDataDTO reqResource)  {
		byte[] fData = Base64.decode(reqResource.getData());
		try {
			return getHash(fData);
		} catch (NoSuchAlgorithmException ex) {
			throw new UncheckedNoSuchAlgorithmException(ex);
		}
	}
	
	protected byte[] getHashReqResourceImpl(final ReqResHttpDTO reqResource) {
		BufferedInputStream btc;
		try {
			btc = new BufferedInputStream(new URL(reqResource.getUrl()).openStream());
	        ByteArrayOutputStream btos = new ByteArrayOutputStream();
			 byte[] bucket = new byte[2048];
			    int numBytesRead;

			    while ((numBytesRead = btc.read(bucket, 0, bucket.length)) != -1) {
			    	btos.write(bucket, 0, numBytesRead);
			    }
			return getHash(btos.toByteArray());
		} catch (MalformedURLException ex) {
			throw new UncheckedMalformedURLException(ex);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		} catch (NoSuchAlgorithmException ex) {
			throw new UncheckedNoSuchAlgorithmException(ex);
		}
	}
	
	protected byte[] getHashReqResourceImpl(final ReqResChecksumDTO reqResource) {
		byte[] fData = Base64.decode(reqResource.getChecksum());
		return fData;
	}
	
	public byte[] getHashFile(Path file) throws NoSuchAlgorithmException, IOException {
		return getHash(Files.readAllBytes(file));
	}
	
	public byte[] getHash(byte[] content) throws NoSuchAlgorithmException {
		final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
		return digest.digest(content);
	}

}
