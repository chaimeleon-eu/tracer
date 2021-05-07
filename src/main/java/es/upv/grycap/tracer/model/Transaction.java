package es.upv.grycap.tracer.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class Transaction<I, O, M> {
	
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
}
