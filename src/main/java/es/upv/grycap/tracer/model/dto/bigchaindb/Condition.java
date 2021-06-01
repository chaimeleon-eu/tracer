package es.upv.grycap.tracer.model.dto.bigchaindb;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class Condition implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = -1037665306865539587L;
	protected Subcondition details;
	protected String uri;

}
