package es.upv.grycap.tracer.model.trace;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

import es.upv.grycap.tracer.exceptions.TraceException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TraceBaseDeserializer extends StdDeserializer<TraceBase> {

	private static final long serialVersionUID = -7474229162175933949L;
	
	public TraceBaseDeserializer() {
		this(null);
	}

	protected TraceBaseDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public TraceBase deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = p.getCodec().readTree(p);
        String version = ((TextNode) node.get("version")).asText();
        TraceVersion v = TraceVersion.valueOf(version);
        if (v == TraceVersion.V1) {
        	ObjectMapper om = new ObjectMapper();
            //om.addMixIn(TraceBase.class, Trace.class);
        	log.info(node.toString());
        	return //p.readValueAs(Trace.class);//
        		//ctxt.readValue(node.traverse(), Trace.class);//
        			om.readValue(node.toString(), es.upv.grycap.tracer.model.trace.v1.Trace.class);
        } else {
        	throw new TraceException("Unhandled trace version " + version);
        }
	}

}
