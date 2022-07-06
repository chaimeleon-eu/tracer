package es.upv.grycap.tracer.model.trace;

/**
 * A trace can change its underlying structure, but since it is stored in a blockchain, the previous entries
 * cannot be updated. 
 * As a result we need to know the version of a trace in order to successfully use it in the web service.
 * 
 * @author Andy S ALic
 *
 */
public enum TraceVersion {	
	
	V1

}
