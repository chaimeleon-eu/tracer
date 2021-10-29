package es.upv.grycap.tracer.model.trace.v1;

import java.time.Instant;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
public class TraceCacheEntry    {

	public enum Status {SUBMITTED, ACCEPTED};

	@Id
	protected long id;
	protected String idTransaction;
	protected Status status;
	protected Instant submitDate;
	protected Trace trace;

}
