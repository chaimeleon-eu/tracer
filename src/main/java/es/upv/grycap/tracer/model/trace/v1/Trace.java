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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer;

import es.upv.grycap.tracer.exceptions.UncheckedExceptionFactory;
import es.upv.grycap.tracer.exceptions.UncheckedInterruptedException;
import es.upv.grycap.tracer.model.dto.ReqDTO;
import es.upv.grycap.tracer.model.trace.TraceBase;
import es.upv.grycap.tracer.model.trace.TraceSummaryBase;
import es.upv.grycap.tracer.model.trace.TraceVersion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@JsonDeserialize(using = TraceDeserializer.class) //as = Trace.class) //using = AsPropertyTypeDeserializer.class)// JsonDeserializer.None.class)
@Slf4j
public class Trace extends TraceBase {
	
	public static final String FNAME_USER_ID = "userId";
	//public static final String FNAME_DATASET_ID = "datasetId";
	public static final String FNAME_TYPE = "type";
	
	public static final TraceVersion VERSION = TraceVersion.V1;
	
	@JsonCreator
	public Trace(@JsonProperty("id") String id, @JsonProperty("timestamp") String timestamp) {
		super(VERSION);
		//id = generateId();
		this.id = id;
		this.timestamp = timestamp;
	}

	

	/**
	 * The ID of the user (person, application, service etc.) that performed the traced action
	 */
	protected String userId;
	/**
	 * The action of a user (person, application, service etc.) represented by this trace
	 */
	protected UserAction userAction;
	
//	public static synchronized String generateId() {
//		try {
//			TimeUnit.MILLISECONDS.sleep(5);
//		} catch (InterruptedException e) {
//			throw new UncheckedInterruptedException(e);
//		}
//		return Long.toString(Instant.now().toEpochMilli());
//	}

	@Override
	public TraceSummaryBase toSummary() {
		TraceSummary trs = new TraceSummary();
		trs.setCallerId(callerId);
		trs.setCreationDate(Instant.ofEpochMilli(Long.parseLong(id)));
		trs.setId(id);
		trs.setTimestamp(timestamp);
		trs.setUserAction(userAction);
		trs.setUserId(userId);
		return trs;
	}

//	@Override
//	public TraceBase fromString(String str) {
//	}

}
