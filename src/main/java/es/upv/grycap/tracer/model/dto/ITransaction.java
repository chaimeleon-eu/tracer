package es.upv.grycap.tracer.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ITransaction<T> {
	
	public String getId();
	
	@JsonIgnore
	public T getTransaction();

}
