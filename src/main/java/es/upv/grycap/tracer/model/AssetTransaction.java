package es.upv.grycap.tracer.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class AssetTransaction extends Asset{

	private static final long serialVersionUID = 4049665330611846640L;
	protected String id;

}
