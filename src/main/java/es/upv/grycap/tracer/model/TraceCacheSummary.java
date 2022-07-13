package es.upv.grycap.tracer.model;

import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import es.upv.grycap.tracer.model.dto.BlockchainType;
import es.upv.grycap.tracer.model.dto.ReqCacheStatus;
import es.upv.grycap.tracer.persistence.BlockchainTypeConverter;
import es.upv.grycap.tracer.persistence.TraceCacheStatusConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table
@Inheritance(strategy = InheritanceType.JOINED)
public class TraceCacheSummary implements IReqCacheEntry {
	
	@Id
	protected UUID id;
	
	/**
	 * Use this field only when the trace has been submitted to the blockchain and a transaction id
	 * has been created by the API
	 */
	protected String transactionId;

	@NotNull
	protected Instant creationDate;

	@NotNull
	protected Instant modificationDate;
	
	@Convert(converter = TraceCacheStatusConverter.class)
	@NotNull
	protected ReqCacheStatus status;
	
	@Column(columnDefinition="TEXT")
	protected String statusMessage;
	
	@Convert(converter = BlockchainTypeConverter.class)
	@NotNull
	protected BlockchainType blockchainType;	

	@NotNull
	protected String callerId;
	
	@NotNull
	protected String traceId;

}
