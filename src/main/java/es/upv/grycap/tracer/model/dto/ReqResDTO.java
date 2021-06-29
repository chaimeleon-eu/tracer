package es.upv.grycap.tracer.model.dto;

import lombok.Setter;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME,
	    include = JsonTypeInfo.As.EXISTING_PROPERTY,
	    property = "contentType"
	)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ReqResFileDataDTO.class, name = "FILE_DATA"),
        @JsonSubTypes.Type(value = ReqResHttpDTO.class, name = "HTTP_FTP"),
        @JsonSubTypes.Type(value = ReqResHashDTO.class, name = "HASH")
})
@NoArgsConstructor
public abstract class ReqResDTO implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = 1L;

	@NotNull(message="The request resource ID cannot be null.")
	@NotBlank(message="The request resource ID cannot be empty.")
	protected String id;
	@NotNull(message="The request resource type cannot be null.")
	protected ReqResContentType contentType;
	@NotNull(message="The request resource name cannot be null.")
	@NotBlank(message="The request resource name cannot be empty.")
	protected String name;
	@NotNull(message="The request resource type cannot be null.")
	@NotBlank(message="The request resource type cannot be empty.")
	protected DatasetResourceType resourceType;
	

}
