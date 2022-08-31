package es.upv.grycap.tracer.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TracerTime {
	
	/**
	 * When true, use the system's time instead of an internet time server
	 */
	protected boolean useLocal;
	/**
	 * A list of NIST Internet Time Servers compliant addresses
	 */
	protected List<String> timeServers;
	/**
	 * Number of retries per server
	 */
	protected int retries;

}
