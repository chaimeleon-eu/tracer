package es.upv.grycap.tracer.service;

import java.util.Map;

import org.springframework.core.env.MissingRequiredPropertiesException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.yaml.snakeyaml.introspector.MissingProperty;

import es.upv.grycap.tracer.model.LogEntryBlock;

@Service
public class LogRequestParser {

//	public LogEntryBlock parseRequest(final LinkedCaseInsensitiveMap<String> logRequest) {
//    	String action  = logRequest.get("action");
//    	String datasetId = logRequest.get("datasetid");
//    	String userId = logRequest.get("userid");
//		if (action != null) {
//			
//		} else {
//			throw new MissingProperty("Request body doesn't contain an action field.");
//		}
//	}
	
}
