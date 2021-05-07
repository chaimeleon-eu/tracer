package es.upv.grycap.tracer.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntryReqResGroupDTO {
	
	protected List<EntryReqResExtDTO> external;
	protected List<EntryReqResExtDTO> internal;

}
