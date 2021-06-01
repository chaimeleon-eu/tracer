package es.upv.grycap.tracer.model.dto.bigchaindb;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.upv.grycap.tracer.model.TraceEntry;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
public class AssetCreate<T extends TraceEntry> extends Asset {

	@JsonIgnore
	private static final long serialVersionUID = -7829702366222988103L;
	protected T data;

}
