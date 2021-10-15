package es.upv.grycap.tracer.model.trace.v1;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@Entity
@DiscriminatorValue("USE_DATASETS_POD")
public class TraceUseDatasets extends Trace {

	@JsonIgnore
	private static final long serialVersionUID = 4774388914989813495L;
	@ElementCollection
	protected List<String> datasetsIds;

}
