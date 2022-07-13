package es.upv.grycap.tracer.model;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import es.upv.grycap.tracer.model.dto.ReqCacheStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TraceCacheDetailed extends TraceCacheSummary implements IReqCacheEntry {
	/**
	 * JSON representation of the request object
	 */
	@Column(columnDefinition="TEXT")
	protected String trace;

}
