package es.upv.grycap.tracer.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public abstract class BlockchainProperties {
	
	protected boolean enabled;
	protected String url;

}
