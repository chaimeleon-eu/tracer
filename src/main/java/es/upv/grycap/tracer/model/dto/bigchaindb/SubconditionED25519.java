package es.upv.grycap.tracer.model.dto.bigchaindb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
public class SubconditionED25519 extends Subcondition {
	
	@JsonIgnore
	private static final long serialVersionUID = -4241810958489170415L;
	
	public SubconditionED25519() {
		this(null);
	}
	
	public SubconditionED25519(String publicKey) {
		this.type = Type.ed25519;
		this.publicKey = publicKey;
	}

	@JsonProperty("public_key")
	protected String publicKey;

}
