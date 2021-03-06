package es.upv.grycap.tracer.model.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import es.upv.grycap.tracer.model.trace.v1.Trace;
import es.upv.grycap.tracer.model.trace.v1.TraceCreateDataset;
import es.upv.grycap.tracer.model.trace.v1.TraceModel;
import es.upv.grycap.tracer.model.trace.v1.UserAction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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
        @JsonSubTypes.Type(value = ReqUpdateDataset.class, name = "UPDATE_DATASET"),
        @JsonSubTypes.Type(value = ReqUseDatasetsDTO.class, name = "USE_DATASETS"),
        @JsonSubTypes.Type(value = ReqCreateModelDTO.class, name = "CREATE_MODEL"),
        @JsonSubTypes.Type(value = ReqUseModelsDTO.class, name = "USE_MODELS")
})
@SuperBuilder
@NoArgsConstructor
public class ReqDTO {
	/**
	 * The action of a user (person, application, service etc.) represented by this trace
	 */
	@NotNull(message="User action field must have a value from the allowed list.")
	protected UserAction userAction;

	/**
	 * The ID of the user (person, application, service etc.) that performed the traced action
	 */
	@NotBlank(message="User field ID must have a value.")
	@NotNull(message="User field ID must have a value.")
	protected String userId;
	
	/**
	 * The blockchains you are interacting with.
	 * Can be missing, all enabled blockchains is assumed
	 */
	protected Set<BlockchainType> blockchains;
}
