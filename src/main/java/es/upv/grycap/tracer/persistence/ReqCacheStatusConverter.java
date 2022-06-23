package es.upv.grycap.tracer.persistence;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import es.upv.grycap.tracer.model.dto.ReqCacheStatus;

@Converter
public class ReqCacheStatusConverter implements AttributeConverter<ReqCacheStatus, String> {

	@Override
	public String convertToDatabaseColumn(ReqCacheStatus attribute) {
		return attribute == null ? null : attribute.name();
	}

	@Override
	public ReqCacheStatus convertToEntityAttribute(String dbData) {
		return dbData == null || dbData.isEmpty() ? null : ReqCacheStatus.valueOf(dbData);
	}

}
