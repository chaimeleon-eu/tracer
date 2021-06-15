package es.upv.grycap.tracer.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME,
	    include = JsonTypeInfo.As.EXISTING_PROPERTY,
	    property = "type"
	)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TraceCreateDataset.class, name = "CREATE_NEW_DATASET"),
        @JsonSubTypes.Type(value = TraceCreateDataset.class, name = "CREATE_NEW_VERSION_DATASET"),
        @JsonSubTypes.Type(value = Trace.class, name = "VISUALIZE_VERSION_DATASET"),
        @JsonSubTypes.Type(value = Trace.class, name = "USE_DATASET_POD"),
        @JsonSubTypes.Type(value = TraceModel.class, name = "CREATE_MODEL_POD"),
        @JsonSubTypes.Type(value = TraceModel.class, name = "USE_MODEL_POD")
})
public class Trace implements Serializable {

	private static final long serialVersionUID = 5757503237019236987L;
	
	public static final String FNAME_USER_ID = "userId";
	public static final String FNAME_DATASET_ID = "datasetId";
	public static final String FNAME_TYPE = "type";
	
	protected String userId;
	protected String datasetId;
	
	protected UserAction type;
	

}
