package es.upv.grycap.tracer.model.dto;

import java.util.List;

import es.upv.grycap.tracer.model.trace.TraceBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
public class RespTraceDTO {

	protected BlockchainType blockchain;
	protected List<TraceBase> traces;

}
