package es.upv.grycap.tracer.model;

import es.upv.grycap.tracer.model.dto.BlockchainType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BlockchainProperties {
	
	protected boolean enabled;
	protected String url;
	protected String name;
	protected int defaultLimit;
	//protected BlockchainType type;

}
