package es.upv.grycap.tracer.model.dto;

import lombok.Setter;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME,
	    include = JsonTypeInfo.As.EXISTING_PROPERTY,
	    property = "type"
	)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ReqResFileDataDTO.class, name = "FILE_DATA"),
        @JsonSubTypes.Type(value = ReqResHttpDTO.class, name = "HTTP"),
        @JsonSubTypes.Type(value = ReqResChecksumDTO.class, name = "CHECKSUM")
})

public abstract class ReqResDTO implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = 1L;
	
	public enum TYPE {FILE_DATA, HTTP, CHECKSUM};

	protected TYPE type;	
	protected String name;	
	protected String path;
	

}
