package es.upv.grycap.tracer.model;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "tracer.time")
@Getter
@Setter
@NoArgsConstructor
public class TracerTime {
	
	/**
	 * When true, use the system's time instead of an Internet time server
	 */
	protected boolean useLocal;
	/**
	 * Timeout for NTP servers if useLocal is set to false
	 */
	protected int ntpTimeout;
	/**
	 * A list of NIST Internet Time Servers compliant addresses
	 */
	protected List<String> timeServers;
	/**
	 * Number of retries per server
	 */
	protected int retries;

}
