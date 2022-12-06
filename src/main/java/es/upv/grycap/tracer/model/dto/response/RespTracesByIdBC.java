package es.upv.grycap.tracer.model.dto.response;

import es.upv.grycap.tracer.model.trace.TraceBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class RespTracesByIdBC extends RespTracesBCBase {

    protected TraceBase trace;
}
