package es.upv.grycap.tracer.model.dto;

import java.net.MalformedURLException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.upv.grycap.tracer.model.exceptions.UncheckedMalformedURLException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
public class ReqResHttpDTO  extends ReqResDTO {
	@JsonIgnore
	private static final long serialVersionUID = 1537637024883221490L;
	
	/**
	 * Url to the available resource
	 */
	@Getter
	@Setter
	@NotNull(message="The URL of the resource can't be null.")
	@NotBlank(message="The URL of the resource can't be empty.")
	//@URL(regexp = "^(http|ftp|https|ftps).*")
	protected String url;
	
//	@Override
//	public String getName() {
//		try {
//			return new URL(url).getFile();
//		} catch (MalformedURLException ex) {
//			throw new UncheckedMalformedURLException(ex);
//		}
//	}
//	
//	@Override
//	public void setName(String name) {}

}
