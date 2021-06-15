package es.upv.grycap.tracer.persistence;

import java.io.IOException;
import java.io.UncheckedIOException;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import es.upv.grycap.tracer.model.dto.bigchaindb.Input;
import es.upv.grycap.tracer.model.dto.bigchaindb.Output;
import es.upv.grycap.tracer.model.dto.bigchaindb.Transaction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransactionConverter<I extends Input, O extends Output, M extends Object> implements AttributeConverter< Transaction<I, O, M>, String>{

	@Override
	public String convertToDatabaseColumn(Transaction<I, O, M> attribute) {
		String tr = null;
        try {
        	tr = new ObjectMapper()
        		      .setSerializationInclusion(Include.NON_NULL)
        		      .registerModule(new JavaTimeModule()).writeValueAsString(attribute);
        } catch (final JsonProcessingException e) {
            log.error("JSON writing error", e);
        }

        return tr;
	}

	@Override
	public Transaction<I, O, M> convertToEntityAttribute(String dbData) {
		Transaction<I, O, M> tr = null;
		try {
			tr = new ObjectMapper()
      		      .setSerializationInclusion(Include.NON_NULL)
      		      .registerModule(new JavaTimeModule()).readValue(dbData, Transaction.class);
		} catch (final IOException e) {
            throw new UncheckedIOException(e);
        }

		return tr;
	}

}
