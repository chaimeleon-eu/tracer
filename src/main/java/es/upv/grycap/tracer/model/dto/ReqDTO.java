package es.upv.grycap.tracer.model.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import es.upv.grycap.tracer.model.Trace;
import es.upv.grycap.tracer.model.TraceCreateDataset;
import es.upv.grycap.tracer.model.TraceModel;
import es.upv.grycap.tracer.model.UserAction;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME,
	    include = JsonTypeInfo.As.EXISTING_PROPERTY,
	    property = "userAction"
	)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ReqCreateDatasetDTO.class, name = "CREATE_NEW_DATASET"),
        @JsonSubTypes.Type(value = ReqCreateDatasetDTO.class, name = "CREATE_NEW_VERSION_DATASET"),
        @JsonSubTypes.Type(value = ReqDTO.class, name = "VISUALIZE_VERSION_DATASET"),
        @JsonSubTypes.Type(value = ReqDTO.class, name = "USE_DATASET_POD"),
        @JsonSubTypes.Type(value = ReqModelDTO.class, name = "CREATE_MODEL_POD"),
        @JsonSubTypes.Type(value = ReqModelDTO.class, name = "USE_MODEL_POD")
})
public class ReqDTO implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = 7613010919109618521L;
	@NotBlank(message="User action field must have a value from the allowed list.")
	@NotNull(message="User action field must have a value from the allowed list.")
	protected UserAction userAction;
	@NotBlank(message="Dataset field ID must have a value.")
	@NotNull(message="Dataset field ID must have a value.")
	protected String datasetId;
	@NotBlank(message="User field ID must have a value.")
	@NotNull(message="User field ID must have a value.")
	protected String userId;
}
