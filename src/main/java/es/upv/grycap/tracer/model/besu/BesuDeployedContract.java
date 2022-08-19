package es.upv.grycap.tracer.model.besu;

import java.time.Instant;

import org.web3j.tx.Contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor

@AllArgsConstructor
public class BesuDeployedContract {
	
	public BesuDeployedContract(String address, String code) {
		this.address = address;
		this.code = code;
		deployDate = Instant.now();
	}
	
	protected String address;
	
	protected String code;
	
	protected Instant deployDate;

}
