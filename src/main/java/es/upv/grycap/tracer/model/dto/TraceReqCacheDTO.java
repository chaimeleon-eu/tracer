package es.upv.grycap.tracer.model.dto;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import es.upv.grycap.tracer.model.trace.TraceReqCacheStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class TraceReqCacheDTO {
	protected UUID id;
	protected TraceReqCacheStatus status;
	protected boolean error;
	protected String message;

}
