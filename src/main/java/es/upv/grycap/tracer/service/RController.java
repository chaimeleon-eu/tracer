package es.upv.grycap.tracer.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.activation.UnsupportedDataTypeException;
import javax.validation.Valid;

import org.apache.commons.codec.binary.Hex;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.fasterxml.jackson.core.JsonProcessingException;

import es.upv.grycap.tracer.model.FilterParams;
import es.upv.grycap.tracer.model.TracerRoles;
import es.upv.grycap.tracer.model.dto.AppInfoDTO;
import es.upv.grycap.tracer.model.dto.DatasetResourceType;
import es.upv.grycap.tracer.model.dto.HashType;
import es.upv.grycap.tracer.model.dto.ReqDTO;
import es.upv.grycap.tracer.model.dto.ReqResContentType;
import es.upv.grycap.tracer.model.dto.RespErrorDTO;
import es.upv.grycap.tracer.model.trace.v1.Trace;
import es.upv.grycap.tracer.model.trace.v1.UserAction;
import lombok.extern.slf4j.Slf4j;

/**
*
* @author Andy S Alic (asalic)
*/
@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000/", maxAge = 3600)
@RequestMapping("/v1")
public class RController {
	
	@Autowired
	protected AppInfoDTO appInfo;
	
	@Autowired
	protected BlockchainManager bcManager;
	
	@Autowired
	protected HashingService hashingService;


    @RequestMapping(value = "/app/info", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<?> getAppInfo(Authentication authentication) throws JsonProcessingException {
        return new ResponseEntity<>(appInfo, new HttpHeaders(), HttpStatus.OK);

    }
    
    @RequestMapping(value = "/traces", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<?> addTrace(Authentication authentication, @Valid @RequestBody ReqDTO logRequest) 
    		throws UnsupportedDataTypeException {
    	String id = getCallerUserId(authentication);
    	bcManager.addTrace(logRequest, id);
        return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NO_CONTENT);
    }
    
//    @RequestMapping(value = "/traces", method = RequestMethod.GET, produces = {"application/json"})
//    public ResponseEntity<?> getTraces(Authentication authentication, 
//    		@PathVariable(name = "userId") String userId,
//    		@RequestParam(name = "callerUserId", required=false) String callerUserId
//    		) throws BadRequest, UnsupportedDataTypeException, MissingServletRequestParameterException {
//
//    	List<Trace> traces = bcManager.getTraceEntries();
//        return new ResponseEntity<>(traces, new HttpHeaders(), HttpStatus.OK);
//    }
    
    @RequestMapping(value = "/traces", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<?> getTracesByActionUser(Authentication authentication, 
    		@RequestParam(name = "userId", required=false) List<String> usersIds,
    		@RequestParam(name = "callerUserId", required=false) List<String> callerUsersIds,
    		@RequestParam(name = "datasetId", required=false) List<String> datasetsIds,
    		@RequestParam(name = "userAction", required=false) List<UserAction> userActions		
    		) throws BadRequest, UnsupportedDataTypeException, MissingServletRequestParameterException {
//    	Set<String> roles = getAuthenticatedUserRoles(authentication);
//    	if (!roles.contains(TracerRoles.TRACER_ADMIN.name())) {
////    		if (userId == null)
////    			return new ResponseEntity<>(new RespErrorDTO("Missing user id path parameter."), new HttpHeaders(), HttpStatus.BAD_REQUEST);
//        	if (userId == null || userId.isBlank() || userId.isEmpty()) {
//        		throw new MissingServletRequestParameterException("userId", "String");
//        	}
//    	}
    	final FilterParams fp = new FilterParams();
    	fp.setCallerUsersIds(callerUsersIds);
    	fp.setDatasetsIds(datasetsIds);
    	fp.setUsersIds(usersIds);
    	fp.setUserActions(userActions);
    	
    	List<Trace> traces = bcManager.getTraces(fp);
        return new ResponseEntity<>(traces, new HttpHeaders(), HttpStatus.OK);
    }
    
//    @RequestMapping(value = "/traces/{userId}/{actionIdName}", method = RequestMethod.GET, produces = {"application/json"})
//    public ResponseEntity<?> getLogsByUserAction(@PathVariable(name = "userId", required = true) String userId,
//    		@PathVariable(name = "actionIdName", required = true) String actionIdName) {
//        return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.OK);
//    }
    
    @RequestMapping(value = "/traces/actions", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<?> getActions(Authentication authentication) {
    	
        return new ResponseEntity<>(UserAction.values(), new HttpHeaders(), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/traces/hashes", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<?> getHashTypes(Authentication authentication) {
        return new ResponseEntity<>(HashType.values(), new HttpHeaders(), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/traces/dataset_resources", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<?> getDatasetResources(Authentication authentication) {
        return new ResponseEntity<>(DatasetResourceType.values(), new HttpHeaders(), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/traces/request_resource_contents", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<?> getRequestResourceContentTypes(Authentication authentication) {
        return new ResponseEntity<>(ReqResContentType.values(), new HttpHeaders(), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/traces/cache", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<?> getTracesCache() {
        return new ResponseEntity<>(ReqResContentType.values(), new HttpHeaders(), HttpStatus.OK);
    }
    
    protected Set<String> getAuthenticatedUserRoles(Authentication authentication) throws UnsupportedDataTypeException {
    	Object principal = authentication.getPrincipal();
    	if (principal instanceof User) {
    		User user = (User) principal;
    		return user.getAuthorities().stream().map(authority -> authority.getAuthority()).collect(Collectors.toSet());
    	} else if (principal instanceof KeycloakPrincipal) {
    		KeycloakPrincipal<KeycloakSecurityContext> pr = (KeycloakPrincipal<KeycloakSecurityContext>)principal;
    		Set<String> roleNames = pr.getKeycloakSecurityContext().getToken().getRealmAccess().getRoles();
    		return roleNames;
    	} else {
    		throw new UnsupportedDataTypeException("The user authentication type is unsupported");
    	}
    }

    protected String getCallerUserId(Authentication authentication) throws UnsupportedDataTypeException {
    	Object principal = authentication.getPrincipal();
    	if (principal instanceof User) {
    		User user = (User) principal;
    		String id = Hex.encodeHexString(hashingService.getHash(user.getUsername().getBytes(), HashType.SHA3_512).getHash());
    		return id;
    	} else if (principal instanceof KeycloakPrincipal) {
    		KeycloakPrincipal<KeycloakSecurityContext> pr = (KeycloakPrincipal<KeycloakSecurityContext>)principal;
    		Set<String> roleNames = pr.getKeycloakSecurityContext().getToken().getRealmAccess().getRoles();

    		return null;
    	} else {
    		throw new UnsupportedDataTypeException("The user authentication type is unsupported");
    	}
    }
	
}
