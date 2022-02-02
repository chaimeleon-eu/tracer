package es.upv.grycap.tracer.model.trace.v1;

import java.io.Serializable;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

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

import es.upv.grycap.tracer.exceptions.UncheckedInterruptedException;
import es.upv.grycap.tracer.model.dto.ReqDTO;
import es.upv.grycap.tracer.model.trace.TraceBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
//@SuperBuilder
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
@MappedSuperclass
@DiscriminatorColumn(name = "userAction", discriminatorType = DiscriminatorType.STRING)
public class Trace extends TraceBase implements Serializable {

	private static final long serialVersionUID = 5757503237019236987L;

	public static final String FNAME_USER_ID = "userId";
	//public static final String FNAME_DATASET_ID = "datasetId";
	public static final String FNAME_TYPE = "type";
	
	public static final String VERSION = "1";
	
	public Trace() {
		super(VERSION);
		id = generateId();
	}

	

	/**
	 * The ID of the user (person, application, service etc.) that performed the traced action
	 */
	protected String userId;
	/**
	 * The ID if the user (person, application, service etc.) that invoked the tracer service with the intention of registering this trace
	 */
	protected String callerId;
	/**
	 * The action of a user (person, application, service etc.) represented by this trace
	 */
	protected UserAction userAction;
	
	public static synchronized String generateId() {
		try {
			TimeUnit.MILLISECONDS.sleep(5);
		} catch (InterruptedException e) {
			throw new UncheckedInterruptedException(e);
		}
		return Long.toString(Instant.now().toEpochMilli());
	}

}
