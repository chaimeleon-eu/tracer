package es.upv.grycap.tracer.model.dto;

import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Configuration
public class AppInfoDTO {
	
	
	
	protected String version;
	protected int yearMin;
	protected int yearMax;

}
