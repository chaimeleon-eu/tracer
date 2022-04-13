package es.upv.grycap.tracer.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Component
@ConfigurationProperties(prefix = "blockchain.besu.private")
@Getter
@Setter
public class BesuProperties extends BlockchainProperties {

}
