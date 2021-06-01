package es.upv.grycap.tracer.model.dto.bigchaindb;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
public abstract class Subcondition implements Serializable {
	
	public enum Type {
		@JsonProperty("ed25519-sha-256")
		ed25519("ed25519-sha-256", 131072); 
//		@JsonProperty("threshold-sha-256")
//		threshold("threshold-sha-256", null);// it is not null, it must be calculated
		
	    private final String value;
	    private final Integer cost;

	    private Type(String value, Integer cost) {
	        this.value = value;
	        this.cost = cost;
	    }

	    public String getValue() {
	        return value;
	    }
	    
	    public Integer getCost() {
	        return cost;
	    }
	    
	    @Override
	    public String toString() {
	         return value;
	    }
	
	};
	
	@JsonIgnore
	private static final long serialVersionUID = -5539567877337008437L;
	
	protected Type type;

}
