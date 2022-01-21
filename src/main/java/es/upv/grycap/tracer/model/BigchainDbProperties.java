package es.upv.grycap.tracer.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class BigchainDbProperties extends BlockchainProperties {
	
	public enum TRANSACTION_MODE {sync, async};

    protected TRANSACTION_MODE transactionModePost;
    protected int defaultAmountTransaction;
    protected String keypairPrivate;
    protected String keypairPublic;
}
