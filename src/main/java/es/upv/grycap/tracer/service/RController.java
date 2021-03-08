package es.upv.grycap.tracer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import es.upv.grycap.tracer.model.dto.AppInfoDTO;
import lombok.extern.slf4j.Slf4j;

/**
*
* @author Andy S Alic (asalic)
*/
@Slf4j
@RestController
public class RController {
	
	@Autowired
	protected AppInfoDTO appInfo;


    @RequestMapping(value = "/app/info", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<?> getAppInfo() throws JsonProcessingException {
        return new ResponseEntity<>(appInfo, new HttpHeaders(), HttpStatus.OK);

    }
    
    @RequestMapping(value = "/logs", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<?> addLog() {
        return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NO_CONTENT);

    }

	
}
