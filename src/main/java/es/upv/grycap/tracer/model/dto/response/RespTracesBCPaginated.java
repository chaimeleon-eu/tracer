package es.upv.grycap.tracer.model.dto.response;

import java.util.List;

import es.upv.grycap.tracer.model.dto.BlockchainType;
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
@SuperBuilder
public class RespTracesBCPaginated<T extends ITraceResponse> extends RespTracesBCBase {
    
	protected List<T> traces;    
    protected Integer countAllTraces;

}
