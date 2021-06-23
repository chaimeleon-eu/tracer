package es.upv.grycap.tracer.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TraceResource implements Serializable {

	private static final long serialVersionUID = -9144643513573832608L;
	/**
	 * Hex String of the checksum
	 */
	protected String nameHash;
	/**
	 * Hex String of the checksum
	 */
	protected String pathHash;
	/**
	 * Hex String of the checksum
	 */
	protected String contentHash;
	protected HashType contentHashType;

}
