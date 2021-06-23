package es.upv.grycap.tracer.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DataHash {
	
	protected byte[] originalData;
	protected byte[] hash;
	protected HashType hashType;

}
