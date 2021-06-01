package es.upv.grycap.tracer.persistence;

import java.io.IOException;
import java.io.UncheckedIOException;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import es.upv.grycap.tracer.model.dto.bigchaindb.Transaction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransactionConverter implements AttributeConverter<Transaction<?, ?, ?>, String>{

	@Override
	public String convertToDatabaseColumn(Transaction<?, ?, ?> attribute) {
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
	public Transaction<?, ?, ?> convertToEntityAttribute(String dbData) {
		Transaction<?, ?, ?> tr = null;
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
