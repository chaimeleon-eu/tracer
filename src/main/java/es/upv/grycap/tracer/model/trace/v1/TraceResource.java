package es.upv.grycap.tracer.model.trace.v1;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import es.upv.grycap.tracer.model.dto.HashType;
import es.upv.grycap.tracer.model.trace.TraceBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TraceResource implements Serializable {

	private static final long serialVersionUID = -9144643513573832608L;
	/**
	 * The ID of the resource, must be anonymized.
	 */
	protected String id;
//	/**
//	 * Base64 encoded String of the hash
//	 */
//	@NotNull
//	@NotEmpty
//	protected String nameHash;
//	@NotEmpty
//	@NotNull
//	protected HashType nameHashType;
//	/**
//	 * Base64 encoded String of the hash of the path
//	 */
//	protected String pathHash;
//	protected HashType pathHashType;
	/**
	 * Base64 encoded String of the hash of the content
	 */
	@NotNull
	@NotEmpty
	protected String contentHash;
	@NotNull
	@NotEmpty
	protected HashType contentHashType;

}
