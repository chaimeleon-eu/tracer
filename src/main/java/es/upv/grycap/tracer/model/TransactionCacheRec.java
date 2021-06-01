package es.upv.grycap.tracer.model;

import java.time.Instant;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import es.upv.grycap.tracer.model.dto.bigchaindb.Input;
import es.upv.grycap.tracer.model.dto.bigchaindb.Output;
import es.upv.grycap.tracer.model.dto.bigchaindb.Transaction;
import es.upv.grycap.tracer.persistence.TransactionConverter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="transaction_cache_rec")
public class TransactionCacheRec<I extends Input, O extends Output, M>    {
	
	public enum Status {SUBMITTED, ACCEPTED};
	
	@Id
	protected long idCache;
	@Column
	protected Status status;
	@Column
	protected Instant submitDate;
	@Column
	protected String idTransaction;
	@Column
	@Convert(converter = TransactionConverter.class)
	protected Transaction<I, O, M> transaction;

}
