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
public class TransactionCacheRec<I extends Input, O extends Output, M extends Object>    {
	
	public enum Status {SUBMITTED, ACCEPTED};
	
	@Id
	protected long idCache;
	protected Status status;
	protected Instant submitDate;
	protected String idTransaction;
	//@Convert(converter = TransactionConverter.class)
	//protected Transaction<I, O, M> transaction;
	protected String transaction;

}
