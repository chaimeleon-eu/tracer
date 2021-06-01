package es.upv.grycap.tracer.service;

import java.security.KeyPair;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NodeKeysManager {
	
	protected KeyPair nodeKeyPair;

	public NodeKeysManager(@Value("${tracer.keypair.private}") String keyPrivPath,
			@Value("${tracer.keypair.public}") String keyPubPath) {
		 //net.i2p.crypto.eddsa.KeyPairGenerator edDsaKpg = new net.i2p.crypto.eddsa.KeyPairGenerator();
		 //nodeKeyPair = edDsaKpg.generateKeyPair();
	}
	
	public KeyPair getKeyPair() {
		return nodeKeyPair;
	}

}
