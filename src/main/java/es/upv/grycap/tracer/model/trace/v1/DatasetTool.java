package es.upv.grycap.tracer.model.trace.v1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Describes the tools used when a dataset is mounted
 * 
 * @author Andy S Alic (asalic)
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DatasetTool {

	/**
	 * name of the tool
	 */
	protected String name;
	
	/**
	 * Version of the tool
	 */
	protected String version;
}
