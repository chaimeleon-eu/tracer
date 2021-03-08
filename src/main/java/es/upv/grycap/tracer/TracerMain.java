package es.upv.grycap.tracer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@ComponentScan({"es.upv.grycap.tracer"})
@EnableConfigurationProperties
@Slf4j
public class TracerMain {

	public static void main(String[] args) {

        SpringApplication application = new SpringApplication(TracerMain.class);
        application.run(args);
	}

}
