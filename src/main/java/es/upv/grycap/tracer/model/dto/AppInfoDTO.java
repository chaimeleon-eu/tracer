package es.upv.grycap.tracer.model.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "tracer.info")
@JsonSerialize(as=AppInfoDTO.class)
public class AppInfoDTO {
	
	
	protected String name;
	protected String version;
	protected String entity;
	protected int yearMin;
	protected int yearMax;

}
