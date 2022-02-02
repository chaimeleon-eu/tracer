package es.upv.grycap.tracer.model.trace;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME,
	    include = JsonTypeInfo.As.EXISTING_PROPERTY,
	    property = "version",
	    visible = true
	)
@JsonSubTypes({
        @JsonSubTypes.Type(value = es.upv.grycap.tracer.model.trace.v1.Trace.class, name = "1")
})
public abstract class TraceBase {
	

	protected String version;
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected String id;
	
	public TraceBase(String version) {
		this.version = version;
	}

}
