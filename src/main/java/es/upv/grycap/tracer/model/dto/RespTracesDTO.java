package es.upv.grycap.tracer.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
public class RespTracesDTO extends RespDTO {
	
	protected List<RespTraceDTO> traces;
}
