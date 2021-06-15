package es.upv.grycap.tracer.model.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.upv.grycap.tracer.model.UserAction;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqDTO implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = 7613010919109618521L;
	protected UserAction userAction;
	protected String datasetId;
	/**
	 * Public Ed25519 key for the user identified by user id 
	 * Encoded as base64
	 */
	//protected String userPublicKey;
	protected String userId;
	protected List<ReqResDTO> resources;
}
