package es.upv.grycap.tracer.model.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.upv.grycap.tracer.model.UserAction;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntryReqDTO implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = 7613010919109618521L;
	protected UserAction userAction;
	protected String userId;
	protected String datasetId;
	protected EntryReqResGroupDTO resources;
}
