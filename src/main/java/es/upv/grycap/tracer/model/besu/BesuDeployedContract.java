package es.upv.grycap.tracer.model.besu;

import java.time.Instant;

import org.web3j.tx.Contract;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
//@NoArgsConstructor
@AllArgsConstructor
public class BesuDeployedContract {
	
	protected String address;
	
	protected String code;
	
	protected String contractClass;
	
	protected Instant deployDate;

}
