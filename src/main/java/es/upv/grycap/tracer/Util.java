package es.upv.grycap.tracer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.Socket;
import java.net.http.HttpClient;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import es.upv.grycap.tracer.exceptions.CaseNotHandledException;
import es.upv.grycap.tracer.model.TraceCacheDetailed;
import es.upv.grycap.tracer.model.TraceCacheOpResult;
import es.upv.grycap.tracer.model.dto.ReqCacheStatus;
import es.upv.grycap.tracer.persistence.ITraceCacheDetailedRepo;
import es.upv.grycap.tracer.service.BlockchainManager;
import es.upv.grycap.tracer.service.caching.TraceCacheOpConsumer;
import es.upv.grycap.tracer.service.caching.TraceCacheOpSubmitter;
import es.upv.grycap.tracer.service.caching.TraceCacheOpUpdater;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	
	public static <T_CLASS> Collection<Field> getAllFieldsAndAncestors(T_CLASS o, boolean modPrivate, boolean modPublic, boolean modProtected) {
	    Class<?> currentClass = o.getClass();
	    Collection<Field> result = new ArrayList<>();
	    while (currentClass != null) {
            Field[] fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                if (modProtected && Modifier.isProtected(field.getModifiers())) {
                    result.add(field);
                } else if (modPublic && Modifier.isPublic(field.getModifiers())) {
                    result.add(field);
                } else if (modPrivate && Modifier.isPrivate(field.getModifiers())) {
                    result.add(field);
                } 
            }
            currentClass = currentClass.getSuperclass();
        }
	    return result;
	}
	
	   public static <T_CLASS> Collection<Field> findFieldsByName(T_CLASS o, String fieldName) {
	        Class<?> currentClass = o.getClass();
	        Collection<Field> result = new ArrayList<>();
	        while (currentClass != null) {
	            Field[] fields = currentClass.getDeclaredFields();
	            Field field = Arrays.stream(fields).filter(e-> e.getName().equals(fieldName)).findFirst().orElse(null);
	            if (field != null) {
	                result.add(field);
	            }
	            currentClass = currentClass.getSuperclass();
	        }
	        return result;
	    }
	
	public static <T_CLASS, T_FVALUE> void updateFieldByName(T_CLASS o, String fieldName, T_FVALUE value) 
	        throws IllegalArgumentException, IllegalAccessException {
	    Collection<Field> fields = Util.<T_CLASS>findFieldsByName(o, fieldName);
	    if (fields.size() > 1) {
	        throw new RuntimeException("Found "+ fields.size() + " fields with name " + fieldName + "in the object's class and its ancestors. Can't update if there are more than one.");
	    } else if (fields.size() == 0) {
	        throw new RuntimeException("Field name '" + fieldName + "' not found in the object's class and its ancestors");
	    } else {
	        Field f = fields.iterator().next();
	        f.setAccessible(true);
	        f.set(o, value);
	    }
	    
	}
	
	public static String getFullStackTrace(Throwable t) {
		  StringWriter sw = new StringWriter();
//		  if (t.getMessage() != null)
//			  sw.append(t.getMessage() + System.getProperty("line.separator"));
//		  if (t.getLocalizedMessage() != null)
//			  sw.append(t.getLocalizedMessage() +  System.getProperty("line.separator"));
		  t.printStackTrace(new PrintWriter(sw));
		  return sw.toString();
	}
	
	public static void loopRequestFutures(ReqCacheStatus status, int retryDelay,
			final BlockchainManager manager, final TraceCacheDetailed rce, 
			final ITraceCacheDetailedRepo reqCacheDetailedRepo,
			final Executor executorCache) {
		if (status == ReqCacheStatus.BLOCKCHAIN_WAITING) {
				//TimeUnit.SECONDS.sleep(retryDelay);
				CompletableFuture.supplyAsync(new TraceCacheOpUpdater(manager, rce.getTransactionId()), 
						executorCache)
						.thenAccept(new TraceCacheOpConsumer(manager, rce.getId(), reqCacheDetailedRepo, executorCache, retryDelay));
		} else if (status == ReqCacheStatus.WAITING 
				|| status == ReqCacheStatus.BLOCKCHAIN_UNAVAILABLE) {
//			try {
				//TimeUnit.SECONDS.sleep(retryDelay);
				CompletableFuture.supplyAsync(new TraceCacheOpSubmitter(reqCacheDetailedRepo, manager, rce.getId(), rce.getTrace(), rce.getCallerId()), 
						executorCache)
						.thenAccept(new TraceCacheOpConsumer(manager, rce.getId(), reqCacheDetailedRepo, executorCache, retryDelay));
//			} catch (InterruptedException e) {
//				log.error(ExceptionUtils.getStackTrace(e));
//			}			
		} else {
			log.info("Status " + status.name() + " not handled by the loop");
		}
	}

}
