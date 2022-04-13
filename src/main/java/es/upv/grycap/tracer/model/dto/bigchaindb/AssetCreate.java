package es.upv.grycap.tracer.model.dto.bigchaindb;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import es.upv.grycap.tracer.model.trace.TraceBase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
public class AssetCreate<T extends TraceBase> extends Asset {

	@JsonIgnore
	private static final long serialVersionUID = -7829702366222988103L;
	protected T data;

	@JsonIgnore
	@Override
	public String getId() {
	    return id;
	}
	
	@JsonProperty
	@Override
	public void setPassword(String id) {
	    this.id = id;
	}
	
}
