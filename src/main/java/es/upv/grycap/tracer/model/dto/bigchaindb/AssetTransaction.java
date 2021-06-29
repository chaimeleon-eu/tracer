package es.upv.grycap.tracer.model.dto.bigchaindb;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
public class AssetTransaction extends Asset{

	@JsonIgnore
	private static final long serialVersionUID = 4049665330611846640L;
	protected String id;

}
