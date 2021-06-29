package es.upv.grycap.tracer.model.dto.bigchaindb;

import java.io.Serializable;

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
public class Fulfills implements Serializable {
	
	@JsonIgnore
	private static final long serialVersionUID = 6102286719829937744L;
	@JsonProperty("transaction_id")
	protected String transactionId;
	@JsonProperty("output_index")
	protected Integer outputIndex;

}
