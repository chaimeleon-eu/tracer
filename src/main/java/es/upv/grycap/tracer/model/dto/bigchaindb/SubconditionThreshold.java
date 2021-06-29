package es.upv.grycap.tracer.model.dto.bigchaindb;

import java.util.List;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.upv.grycap.tracer.model.dto.bigchaindb.Subcondition.Type;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
public class SubconditionThreshold extends Subcondition {
	
	@JsonIgnore
	private static final long serialVersionUID = -2743025156080653339L;
	
	public SubconditionThreshold() {
		//this.type = Type.threshold;
	}
	protected Integer threshold;
	protected List<SubconditionThreshold> subconditions;

}
