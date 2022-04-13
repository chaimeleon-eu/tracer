package es.upv.grycap.tracer.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.KeyPairGenerator;
import net.i2p.crypto.eddsa.spec.EdDSAGenParameterSpec;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveSpec;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;

//@Service
@Slf4j
public class NodeKeysManager {
	
	/**
	 * Starting position of the Ed25519 public key bytes
	 * in a SSH base64 encoded key
	 */
	public static final int POS_PUB_KEY_B64_SSH = 19;
	
	protected KeyPair nodeKeyPair;
	
	protected String keyPrivPath;
	protected String keyPubPath;
	
	public NodeKeysManager(String keyPrivPath, String keyPubPath) {
		this.keyPrivPath = keyPrivPath;
		this.keyPubPath = keyPubPath;
	}
	
//	@PostConstruct
	protected void init() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException {
		if (Files.exists(Path.of(keyPubPath)) && Files.exists(Path.of(keyPrivPath))) {
			log.info("Public and private found, load them");
			byte[] pubk = FileUtils.readFileToByteArray(new File(keyPubPath));
			log.info("pubk len " + pubk.length);
			byte[] privk = FileUtils.readFileToByteArray(new File(keyPrivPath));
			log.info("privk len " + privk.length);
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubk);
			EdDSAPublicKey pubKey = new EdDSAPublicKey(pubKeySpec);
			PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(privk);
			EdDSAPrivateKey privKey = new EdDSAPrivateKey(privKeySpec);
			nodeKeyPair = new KeyPair(pubKey, privKey);
		} else {
			log.info("Public and/or private keys not found, generate them");
			  //Generate ED25519-SHA-256 KeyPair and Signer
			  net.i2p.crypto.eddsa.KeyPairGenerator edDsaKpg = new net.i2p.crypto.eddsa.KeyPairGenerator();
			  EdDSANamedCurveSpec ed25519 = EdDSANamedCurveTable.getByName("Ed25519");
			  //EdDSAGenParameterSpec spec = new EdDSAGenParameterSpec();
			  //EdDSANamedCurveSpec spec = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519);
			  //spec.
			  edDsaKpg.initialize(256, SecureRandom.getInstanceStrong());
			  nodeKeyPair = edDsaKpg.generateKeyPair();
				log.info("Save generate pub key as " + keyPubPath);
			  FileUtils.writeByteArrayToFile(new File(keyPubPath), nodeKeyPair.getPublic().getEncoded());
				log.info("Save generate priv key as " + keyPrivPath);
			  FileUtils.writeByteArrayToFile(new File(keyPrivPath), nodeKeyPair.getPrivate().getEncoded());
		}
		
		
	}
	
	public KeyPair getKeyPair() {
		return nodeKeyPair;
	}
	
	protected byte[] getPubKey(InputStream in) throws IOException {
		String content = IOUtils.toString(in, StandardCharsets.UTF_8);
		String[] parts = content.split(" ");
		if (parts.length != 3) 
			throw new InvalidObjectException("The content of the public key file is invalid. "
					+ "There should be exactly three strings separated by a single space. "
					+ "Curently, there are " + parts.length);
		byte[] keyEnc = Base64.getDecoder().decode(parts[1]);
		return Arrays.copyOfRange(keyEnc, POS_PUB_KEY_B64_SSH, keyEnc.length);
	}
	
	protected byte[] getPrivKey(InputStream in) throws IOException {
		String content = IOUtils.toString(in, StandardCharsets.UTF_8);
		String[] parts = content.split("\n");
		if (parts.length < 3) 
			throw new InvalidObjectException("The content of the private key file is invalid. "
					+ "There should be at least three strings separated by new line. "
					+ "Curently, there are " + parts.length);
		return Base64.getDecoder().decode(String.join("", Arrays.copyOfRange(parts, 1, parts.length-1)));
	}

}
