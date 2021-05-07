package es.upv.grycap.tracer.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class AssetCreate<T> extends Asset {

	private static final long serialVersionUID = -7829702366222988103L;
	protected T data;

}
