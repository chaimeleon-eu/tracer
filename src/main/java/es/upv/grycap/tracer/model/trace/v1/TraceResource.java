package es.upv.grycap.tracer.model.trace.v1;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.sun.istack.NotNull;

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
@Entity
public class TraceResource implements Serializable {

	private static final long serialVersionUID = -9144643513573832608L;
	/**
	 * The ID of the file.
	 * Must protect file information
	 */
	@Id
	protected String id;
	/**
	 * Base64 encoded String of the hash
	 */
	protected String nameHash;
	@NotNull
	protected HashType nameHashType;
	/**
	 * Base64 encoded String of the hash
	 */
	protected String pathHash;
	@NotNull
	protected HashType pathHashType;
	/**
	 * Base64 encoded String of the hash
	 */
	protected String contentHash;
	@NotNull
	protected HashType contentHashType;

}
