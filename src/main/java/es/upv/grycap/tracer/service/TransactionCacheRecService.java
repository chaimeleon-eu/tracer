package es.upv.grycap.tracer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import es.upv.grycap.tracer.model.dto.bigchaindb.Transaction;
import es.upv.grycap.tracer.persistence.TransactionCacheRecRepository;

@Service
public class TransactionCacheRecService {
	
	@Autowired
	protected TransactionCacheRecRepository tcrRepository;
	@Autowired
	protected BlockchainManager bs;
	
	@Scheduled(fixedDelay=1000)
    private void checkStatusTransaction() {
		tcrRepository.getAllTransactionsIds()
			.forEach(t -> {
				Transaction<?, ?, ?> tr = bs.getTransactionById(t);
				tr.getId();
			});
    }

}
