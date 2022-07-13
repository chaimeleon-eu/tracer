package es.upv.grycap.tracer.persistence;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import es.upv.grycap.tracer.model.dto.BlockchainType;
import es.upv.grycap.tracer.model.dto.ReqCacheStatus;

@Converter
public class ReqCacheBlockchainTypeConverter implements AttributeConverter<BlockchainType, String> {

	@Override
	public String convertToDatabaseColumn(BlockchainType attribute) {
		return attribute == null ? null : attribute.name();
	}

	@Override
	public BlockchainType convertToEntityAttribute(String dbData) {
		return dbData == null || dbData.isEmpty() ? null : BlockchainType.valueOf(dbData);
	}

}
