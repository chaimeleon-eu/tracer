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
@SuperBuilder
public abstract class ReqResDTO implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = 1L;

	/**
	 * The id of the submitted resource with the request to add a trace.
	 * It has to uniquely identify this resource on the system.
	 * The uniqueness is not verified by this service.
	 */
	@NotNull(message="The request resource ID cannot be null.")
	@NotBlank(message="The request resource ID cannot be empty.")
	protected String id;
	
	/**
	 * An enum identifier of the resource stored by this object.
	 */
	@NotNull(message="The request resource type cannot be null.")
	protected ReqResContentType contentType;
	
	/**
	 * The name of the resource
	 */
//	@NotNull(message="The request resource name cannot be null.")
//	@NotBlank(message="The request resource name cannot be empty.")
//	protected String name;
	
//	/**
//	 *  The description of the data referred to by this resource object.
//	 *  Can be null.
//	 */
//	protected String description;
	
//	/**
//	 * The actual type of the data described by this resource
//	 */
//	@NotNull(message="The request resource type cannot be null.")
//	@NotBlank(message="The request resource type cannot be empty.")
//	protected DatasetResourceType resourceType;
	

}
