package es.upv.grycap.tracer;

import java.net.Socket;
import java.net.http.HttpClient;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;

public class Util {

	public static HttpClient getHttpClient(boolean disableSSLVerification)
			throws NoSuchAlgorithmException, KeyManagementException {
		if (disableSSLVerification) {
			TrustManager[] trustAllCerts = new TrustManager[] { new X509ExtendedTrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}

				public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}

				public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}

				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket)
						throws CertificateException {}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket)
						throws CertificateException {}

				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
						throws CertificateException {}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine)
						throws CertificateException {}
			} };
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			return HttpClient.newBuilder().sslContext(sc).build();
		} else {
			return HttpClient.newHttpClient();
		}
	}

}
