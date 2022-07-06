package es.upv.grycap.tracer.model.dto.bigchaindb;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import es.upv.grycap.tracer.model.dto.ITransaction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class Transaction<I extends Input, O extends Output, M> implements ITransaction<Object> {
	
	public enum Operation {CREATE, TRANSFER, VALIDATOR_ELECTION, CHAIN_MIGRATION_ELECTION, VOTE};
	public static final String VER_2 = "2.0";
	
	protected String id;
	@Builder.Default
	protected String version = VER_2;
	protected List<I> inputs;
	protected List<O> outputs;
	protected Operation operation;
	protected Asset asset;
	protected M metadata;
	@Override
	public Object getTransaction() {
		// TODO Auto-generated method stub
		return null;
	}
}
