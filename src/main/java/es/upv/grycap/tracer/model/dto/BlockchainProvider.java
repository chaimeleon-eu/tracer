package es.upv.grycap.tracer.model.dto;

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

}
