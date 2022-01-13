package es.upv.grycap.tracer.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import es.upv.grycap.tracer.exceptions.HashTypeNotSupportedException;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum HashType {
	
	@JsonProperty("SHA3_256")
	SHA3_256("SHA3-256"),
	@JsonProperty("SHA3_512")
	SHA3_512("SHA3-512");
	
	/**
	 * Algorithm ID must be available to Java
	 */
	@JsonIgnore
	public String algorithmId;
	
	private HashType(String algorithmId) {
		this.algorithmId = algorithmId;
	}
	
	@Override
	public String toString() {
		return name();
	}
	
	public static HashType fromAlgorithmId(final String algorithmId) {
		for (HashType ht: HashType.values()) {
			if (ht.algorithmId.equalsIgnoreCase(algorithmId))
				return ht;
		}
		throw new HashTypeNotSupportedException("Hash type using JAVA hash algorithm ID " + algorithmId + " not supported;");
	}

}
