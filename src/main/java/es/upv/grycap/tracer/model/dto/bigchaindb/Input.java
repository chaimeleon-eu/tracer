package es.upv.grycap.tracer.model.dto.bigchaindb;

import java.io.Serializable;
import java.util.List;

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
public class Input implements Serializable {
	@JsonIgnore
	private static final long serialVersionUID = -3212913438118205288L;
	protected Fulfills fulfills;
	@JsonProperty("owners_before")
	protected List<String> ownersBefore;
	protected String fulfillment;
}
