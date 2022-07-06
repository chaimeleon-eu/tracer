package es.upv.grycap.tracer.model.dto;

public interface ITransaction<T> {
	
	public String getId();
	
	public T getTransaction();

}
