package es.upv.grycap.tracer.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NodeKeysManagerTest extends NodeKeysManager {

	public NodeKeysManagerTest() {
		super(null, null);
	}

	public static final String PUB_KEY_STR_FULL = 
			"ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIPIQI7aMYFnxszhYzEin3DQ05yxlOX0tF9Bog/8Gtm6G test@test";
	
	public static final String PUB_KEY_ONLY_HEX = 
			"f21023b68c6059f1b33858cc48a7dc3434e72c65397d2d17d06883ff06b66e86";
	
	public static final String PRIV_KEY_STR_FULL = 
			"-----BEGIN OPENSSH PRIVATE KEY-----\n"
			+ "b3BlbnNzaC1rZXktdjEAAAAABG5vbmUUJFGEbm9uZQAAAAAAAAABAAAAMwAAAAtzc2gtZW\n"
			+ "QyNTUxOQAAACDy8iO2jGBZ8bM4erZIp9y6NOfCRzl9HhfQaIPPBrYXhgAAAJgHDN8kBwzf\n"
			+ "JAAAAAtvb2gtZWQyURUxOQ345CDy8iO2jGBZ8bM4erZIp9y6NOfCRzl9HhfQaIOOBrYXhg\n"
			+ "BBBECjVl1iIAidewiJ/7SvJuNyY0x4isQoL6VSn51pPEQkuPLyI7aMYFnxszh6topy3Lo0\n"
			+ "58JHOX0eF9Bog88GtheGAAAAEGFzYWxpY0Bhc2FiTXJ2ZXIBAgMEBQ==\n"
			+ "-----END OPENSSH PRIVATE KEY-----";
	
	public static final String PRIV_KEY_ONLY_HEX = 
			"6f70656e7373682d6b65792d763100000000046e6f6e65142451846e6f6e65000000000000000"
			+ "1000000330000000b7373682d6564323535313900000020f2f223b68c6059f1b3387ab648a7"
			+ "dcba34e7c247397d1e17d06883cf06b6178600000098070cdf24070cdf240000000b6f6f682"
			+ "d656432511531390df8e420f2f223b68c6059f1b3387ab648a7dcba34e7c247397d1e17d068"
			+ "838e06b6178600410440a3565d6220089d7b0889ffb4af26e372634c788ac4282fa5529f9d6"
			+ "93c4424b8f2f223b68c6059f1b3387ab68a72dcba34e7c247397d1e17d06883cf06b6178600"
			+ "0000106173616c696340617361624d727665720102030405";
	
	@Test
	public void test_pub_key_read() throws IOException {
		InputStream stream = new ByteArrayInputStream(PUB_KEY_STR_FULL.getBytes(StandardCharsets.UTF_8));
		byte[] key = getPubKey(stream);
		Assertions.assertEquals(PUB_KEY_ONLY_HEX, Hex.encodeHexString(key));
		stream.close();
	}
	
	@Test
	public void test_priv_key_read() throws IOException {
		InputStream stream = new ByteArrayInputStream(PRIV_KEY_STR_FULL.getBytes(StandardCharsets.UTF_8));
		byte[] key = getPrivKey(stream);
		Assertions.assertEquals(PRIV_KEY_ONLY_HEX, Hex.encodeHexString(key));
		stream.close();
	}

}
