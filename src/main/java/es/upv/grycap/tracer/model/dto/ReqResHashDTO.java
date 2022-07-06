package es.upv.grycap.tracer.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ReqResHashDTO extends ReqResDTO {
	@JsonIgnore
	private static final long serialVersionUID = 3974625444759531622L;
	
	
	/**
	 * Base64 encoded hash
	 */
	@NotNull(message="The Base64 encoded hash of the data can't be null.")
	@NotBlank(message="The Base64 encoded hash of the data can't be blank.")
	protected String hash;
	@NotNull(message="The hash type of the hashed data can't be null.")
	protected HashType hashType;

}
