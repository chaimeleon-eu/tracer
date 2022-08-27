package es.upv.grycap.tracer.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
	
	@Getter
	@Setter
	@NoArgsConstructor
	public static class ContractInfo {
		
		protected String code;
		protected String deployed;
	}

	
	protected ContractInfo contract;
	protected WalletInfo wallet;
}
