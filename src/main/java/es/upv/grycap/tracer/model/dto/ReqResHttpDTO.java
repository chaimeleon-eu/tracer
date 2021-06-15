package es.upv.grycap.tracer.model.dto;

import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.upv.grycap.tracer.model.exceptions.UncheckedMalformedURLException;
import lombok.Getter;
import lombok.Setter;


public class ReqResHttpDTO  extends ReqResDTO {
	@JsonIgnore
	private static final long serialVersionUID = 1537637024883221490L;
	
	/**
	 * Url to the available resource
	 */
	@Getter
	@Setter
	protected String url;
	
	@Override
	public String getName() {
		try {
			return new URL(url).getFile();
		} catch (MalformedURLException ex) {
			throw new UncheckedMalformedURLException(ex);
		}
	}
	
	@Override
	public void setName(String name) {}

}
