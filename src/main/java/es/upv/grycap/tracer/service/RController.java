package es.upv.grycap.tracer.service;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import es.upv.grycap.tracer.model.HashType;
import es.upv.grycap.tracer.model.Trace;
import es.upv.grycap.tracer.model.UserAction;
import es.upv.grycap.tracer.model.dto.AppInfoDTO;
import es.upv.grycap.tracer.model.dto.ReqDTO;
import lombok.extern.slf4j.Slf4j;

/**
*
* @author Andy S Alic (asalic)
*/
@Slf4j
@RestController
@RequestMapping("/v1")
public class RController {
	
	@Autowired
	protected AppInfoDTO appInfo;
	
	@Autowired
	protected BlockchainManager bcManager;


    @RequestMapping(value = "/app/info", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<?> getAppInfo() throws JsonProcessingException {
        return new ResponseEntity<>(appInfo, new HttpHeaders(), HttpStatus.OK);

    }
    
    @RequestMapping(value = "/traces", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<?> addLog(@Valid @RequestBody ReqDTO logRequest) {
    	bcManager.addEntry(logRequest);
        return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NO_CONTENT);

    }
    
    @RequestMapping(value = "/traces/{userId}", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<?> getLogsByUser(@PathVariable(name = "userId", required = true) String userId) {
    	List<Trace> traces = bcManager.getTraceEntriesByUserId(userId);
        return new ResponseEntity<>(traces, new HttpHeaders(), HttpStatus.OK);
    }
    
//    @RequestMapping(value = "/traces/{userId}/{actionIdName}", method = RequestMethod.GET, produces = {"application/json"})
//    public ResponseEntity<?> getLogsByUserAction(@PathVariable(name = "userId", required = true) String userId,
//    		@PathVariable(name = "actionIdName", required = true) String actionIdName) {
//        return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.OK);
//    }
    
    @RequestMapping(value = "/traces/actions", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<?> getActions() {
        return new ResponseEntity<>(UserAction.values(), new HttpHeaders(), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/traces/hash_types", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<?> getHashTypes() {
        return new ResponseEntity<>(HashType.values(), new HttpHeaders(), HttpStatus.OK);
    }

	
}
