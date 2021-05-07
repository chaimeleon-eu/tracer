package es.upv.grycap.tracer.service;

import java.time.Duration;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import es.upv.grycap.tracer.model.dto.EntryReqDTO;
import es.upv.grycap.tracer.model.exceptions.BigchaindbException;
import reactor.core.publisher.Mono;

@Service
public class BigchainDbStorage implements BlockchainStorage {
	
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(20);
    //protected enum TransactionMode {sync, commit, async}
	
	protected WebClient wb;
	
	@Value("${blockchaindb.transactionModePost}") 
	protected String transactionModePost;
	
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

}
