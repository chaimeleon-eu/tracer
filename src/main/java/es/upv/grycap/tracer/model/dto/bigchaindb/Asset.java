package es.upv.grycap.tracer.model.dto.bigchaindb;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@NoArgsConstructor
public class Asset implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = -7910823798763765300L;
	
	protected String id;
	
	@JsonIgnore
	public String getId() {
	    return id;
	}
	@JsonProperty
	public void setPassword(String id) {
	    this.id = id;
	}

}
