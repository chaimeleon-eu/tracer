package es.upv.grycap.tracer.model.dto.bigchaindb;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;

@Getter
public class Fullfilment implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = 6579816539950151605L;
	
	public Fullfilment(String publicKey) {
		this.publicKey = publicKey;
	}
	
	@Builder.Default
	protected String type = "ed25519-sha-256";
	@JsonProperty("public_key")
	protected String publicKey;
	
	

}
