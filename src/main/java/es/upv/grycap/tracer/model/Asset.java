package es.upv.grycap.tracer.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public abstract class Asset implements Serializable {

	private static final long serialVersionUID = -7910823798763765300L;

}
