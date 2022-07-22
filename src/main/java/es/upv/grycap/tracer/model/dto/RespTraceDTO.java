package es.upv.grycap.tracer.model.dto;

import java.util.List;

import es.upv.grycap.tracer.model.trace.ITraceResponse;
import es.upv.grycap.tracer.model.trace.TraceBase;
import es.upv.grycap.tracer.model.trace.TraceSummaryBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RespTraceDTO<T extends ITraceResponse> {

	protected BlockchainType blockchain;
	protected List<T> traces;

}
