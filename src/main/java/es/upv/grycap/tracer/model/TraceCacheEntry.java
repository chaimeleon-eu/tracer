package es.upv.grycap.tracer.model;

import java.time.Instant;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import es.upv.grycap.tracer.model.dto.bigchaindb.Input;
import es.upv.grycap.tracer.model.dto.bigchaindb.Output;
import es.upv.grycap.tracer.model.dto.bigchaindb.Transaction;
import es.upv.grycap.tracer.persistence.TransactionConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class TraceCacheEntry<T extends Trace>    {
	
	public enum Status {SUBMITTED, ACCEPTED};
	
	@Id
	protected long id;
	protected String idTransaction;
	protected Status status;
	protected Instant submitDate;
	protected T trace;

}
