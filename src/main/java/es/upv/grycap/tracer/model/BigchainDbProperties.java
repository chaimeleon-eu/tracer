package es.upv.grycap.tracer.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Component
@ConfigurationProperties(prefix = "blockchain.bigchaindb.private")
@Getter
@Setter
public class BigchainDbProperties extends BlockchainProperties {
	
	public enum TRANSACTION_MODE {sync, async};

    protected TRANSACTION_MODE transactionModePost;
    protected int defaultAmountTransaction;
    protected String keypairPrivate;
    protected String keypairPublic;
}
