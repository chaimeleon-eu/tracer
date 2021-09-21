package es.upv.grycap.tracer.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import es.upv.grycap.tracer.model.dto.ReqDTO;
import es.upv.grycap.tracer.model.dto.UserAction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME,
	    include = JsonTypeInfo.As.EXISTING_PROPERTY,
	    property = "userAction",
	    visible = true
	)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TraceCreateDataset.class, name = "CREATE_NEW_DATASET"),
        @JsonSubTypes.Type(value = TraceCreateVersionDataset.class, name = "CREATE_VERSION_DATASET"),
        @JsonSubTypes.Type(value = TraceDataset.class, name = "VISUALIZE_VERSION_DATASET"),
        @JsonSubTypes.Type(value = TraceUseDatasets.class, name = "USE_DATASETS_POD"),
        @JsonSubTypes.Type(value = TraceCreateModel.class, name = "CREATE_MODEL"),
        @JsonSubTypes.Type(value = TraceUseModels.class, name = "USE_MODELS")
})
@NoArgsConstructor
@MappedSuperclass
@DiscriminatorColumn(name = "userAction", discriminatorType = DiscriminatorType.STRING)
public abstract class Trace implements Serializable {

	private static final long serialVersionUID = 5757503237019236987L;

	public static final String FNAME_USER_ID = "userId";
	//public static final String FNAME_DATASET_ID = "datasetId";
	public static final String FNAME_TYPE = "type";

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;

	protected String userId;

	protected UserAction userAction;

}
