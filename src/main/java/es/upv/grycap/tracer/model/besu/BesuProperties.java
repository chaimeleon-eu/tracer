package es.upv.grycap.tracer.model.besu;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import es.upv.grycap.tracer.model.BlockchainProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Component
@ConfigurationProperties(prefix = "blockchain.besu.private")
@Getter
@Setter
public class BesuProperties extends BlockchainProperties {
	
	@Getter
	@Setter
	@NoArgsConstructor
	public static class WalletInfo {
		
		protected String password;
		protected String path;
	}
	
	
	/**
	 * Information about a contract supported by the platform
	 * @author Andy S Alic
	 * 
	 */
	@Getter
	@Setter
	@NoArgsConstructor
	public static class ContractInfo {
		
		protected String name;		
		protected ContractInfoEnable enable;
		protected String deployed;
	}
	
	/**
	 * What operations should be enable for a contract
	 * @author Andy S Alic
	 *
	 */
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ContractInfoEnable {
		
		/**
		 * Contract allows the addition of new traces
		 */
		protected boolean add;		

		
		/**
		 * Contract allows reading of the existing traces
		 */
		protected boolean read;
	}

	
	protected List<ContractInfo> contracts;
	protected WalletInfo wallet;
}
