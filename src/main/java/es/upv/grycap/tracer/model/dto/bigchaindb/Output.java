package es.upv.grycap.tracer.model.dto.bigchaindb;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class Output implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = -13283828857249004L;
	
	protected Condition condition;
	@JsonProperty("public_keys")
	protected List<String> publicKeys;
	protected Integer amount;

}
