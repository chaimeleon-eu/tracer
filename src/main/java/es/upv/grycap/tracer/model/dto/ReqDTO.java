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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME,
	    include = JsonTypeInfo.As.EXISTING_PROPERTY,
	    property = "userAction",
	    visible = true
	)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ReqCreateDatasetDTO.class, name = "CREATE_DATASET"),
        @JsonSubTypes.Type(value = ReqCreateVersionDatasetDTO.class, name = "CREATE_VERSION_DATASET"),
        @JsonSubTypes.Type(value = ReqDatasetDTO.class, name = "VISUALIZE_VERSION_DATASET"),
        @JsonSubTypes.Type(value = ReqUseDatasetsDTO.class, name = "USE_DATASETS_POD"),
        @JsonSubTypes.Type(value = ReqCreateModelDTO.class, name = "CREATE_MODEL"),
        @JsonSubTypes.Type(value = ReqUseModelsDTO.class, name = "USE_MODELS")
})
public class ReqDTO implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = 7613010919109618521L;
	@NotNull(message="User action field must have a value from the allowed list.")
	protected UserAction userAction;
	@NotBlank(message="User field ID must have a value.")
	@NotNull(message="User field ID must have a value.")
	protected String userId;
}
