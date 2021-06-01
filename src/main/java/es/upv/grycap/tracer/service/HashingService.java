package es.upv.grycap.tracer.service;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;

@Service
public class HashingService {
	
	public byte[] getHashFile(Path file) throws NoSuchAlgorithmException, IOException {
		return getHash(Files.readAllBytes(file));
	}
	
	public byte[] getHash(byte[] content) throws NoSuchAlgorithmException {
		final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
		return digest.digest(content);
	}

}
