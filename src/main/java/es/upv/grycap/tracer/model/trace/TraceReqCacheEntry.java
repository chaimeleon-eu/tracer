package es.upv.grycap.tracer.model.trace;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
public class TraceReqCacheEntry    {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	protected UUID id;
	@NotBlank(message = "Caller ID can't be blank")
	@NotNull(message = "Caller ID can't be null")
	protected String callerId;
	@NotBlank(message = "Request body can't be blank")
	@NotNull(message = "Request body can't be null")
	protected String body;
	@NotNull(message = "Status body can't be null")
	@Enumerated(EnumType.STRING)
	protected TraceReqCacheStatus status;
	@NotNull(message = "Creation date can't be null")
	protected Instant creationDate;
	@NotNull(message = "Modification date can't be null")
	protected Instant modificationDate;
	protected boolean error;
	protected String message;

}
