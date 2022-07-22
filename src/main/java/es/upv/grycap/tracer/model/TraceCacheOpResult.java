package es.upv.grycap.tracer.model;

import es.upv.grycap.tracer.model.dto.ReqCacheStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
public class TraceCacheOpResult {
	
	protected String msg;
	protected ReqCacheStatus status;
	
	protected String transactionId;

}
