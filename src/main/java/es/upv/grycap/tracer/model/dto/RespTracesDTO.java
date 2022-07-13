package es.upv.grycap.tracer.model.dto;

import java.util.List;

import es.upv.grycap.tracer.model.trace.ITraceResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RespTracesDTO<T extends ITraceResponse> extends RespDTO {
	
	protected List<RespTraceDTO<T>> traces;
}
