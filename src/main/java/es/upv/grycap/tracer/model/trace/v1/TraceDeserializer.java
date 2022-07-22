package es.upv.grycap.tracer.model.trace.v1;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.gson.Gson;

import es.upv.grycap.tracer.exceptions.TraceException;
import es.upv.grycap.tracer.model.trace.TraceBase;
import es.upv.grycap.tracer.model.trace.TraceVersion;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TraceDeserializer extends StdDeserializer<Trace> {
	
	private static final long serialVersionUID = -2357523000345257069L;

	public TraceDeserializer() {
		this(null);
	}

	protected TraceDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public Trace deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
		 JsonNode node = p.getCodec().readTree(p);
	        String ua = ((TextNode) node.get("userAction")).asText();
	        UserAction userAction = UserAction.valueOf(ua);
        	Gson gson = new Gson();
	        if (userAction == UserAction.CREATE_DATASET) {
	        	return gson.fromJson(node.toString(), TraceCreateDataset.class);
	        } else if (userAction == UserAction.UPDATE_DATASET) {
	        	return gson.fromJson(node.toString(), TraceUpdateDataset.class);
	        } else if (userAction == UserAction.CREATE_MODEL) {
	        	return gson.fromJson(node.toString(), TraceCreateModel.class);
	        } else if (userAction == UserAction.USE_DATASETS) {
	        	return gson.fromJson(node.toString(), TraceUseDatasets.class);
	        } else if (userAction == UserAction.USE_MODELS) {
	        	return gson.fromJson(node.toString(), TraceUseModels.class);
	        } else {
	        	throw new TraceException("Unhandled trace user action " + ua);
	        }
	}

}
