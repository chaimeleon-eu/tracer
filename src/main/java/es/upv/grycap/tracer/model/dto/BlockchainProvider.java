package es.upv.grycap.tracer.model.dto;

import es.upv.grycap.tracer.model.BlockchainProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class BlockchainProvider {
	
	protected BlockchainType type;
	
	protected String name;
	
	protected boolean enabled;
	
	protected BlockchainProperties properties;

}
