package es.upv.grycap.tracer.model.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReqUseDatasetsDTO extends ReqDTO {

	@JsonIgnore
	private static final long serialVersionUID = -5272007925810580184L;
	
	@NotNull(message="The list of datasets' ids cannot be null.")
	@NotEmpty(message="The list of datasets' ids cannot be empty.")
	protected List<String> datasetsIds;

}
