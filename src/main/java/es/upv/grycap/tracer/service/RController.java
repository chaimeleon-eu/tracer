package es.upv.grycap.tracer.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
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
import es.upv.grycap.tracer.model.IReqCacheEntry;
import es.upv.grycap.tracer.model.ReqCacheEntrySummary;
import es.upv.grycap.tracer.model.TracerRoles;
import es.upv.grycap.tracer.model.dto.AppInfoDTO;
import es.upv.grycap.tracer.model.dto.BlockchainType;
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
//@CrossOrigin(origins = "http://asaserver.i3m.upv.es:3005", maxAge = 3600)
@RequestMapping("/api/v1")
public class RController {
	
	protected AppInfoDTO appInfo;
	
	protected BlockchainManagerProxy bcManager;
		
	public RController(@Autowired AppInfoDTO appInfo, @Autowired BlockchainManagerProxy bcManager) {
		this.appInfo = appInfo;
		this.bcManager = bcManager;
	}


    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<?> getWebServiceInfo() throws JsonProcessingException {
        return new ResponseEntity<>(appInfo, new HttpHeaders(), HttpStatus.OK);

    }
	
//    @RequestMapping(value = "/app/info", method = RequestMethod.GET, produces = {"application/json"})
//    public ResponseEntity<?> getAppInfo(Authentication authentication) throws JsonProcessingException {
//        return new ResponseEntity<>(appInfo, new HttpHeaders(), HttpStatus.OK);
//
//    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<?> login(Authentication authentication, @Valid @RequestBody ReqDTO logRequest) 
    		throws UnsupportedDataTypeException {
    	
        return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.ACCEPTED);
    }
    
    @RequestMapping(value = "/traces", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<?> postTrace(Authentication authentication, @Valid @RequestBody ReqDTO logRequest) 
    		throws UnsupportedDataTypeException {
    	final Collection<ReqCacheEntrySummary> summaries = bcManager.addTrace(authentication, logRequest);
        return new ResponseEntity<>(summaries, new HttpHeaders(), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/traces/cache", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<?> getTracesCache(Authentication authentication,
    		@RequestParam(name = "detailed", required=false) Boolean detailed) 
    		throws UnsupportedDataTypeException {
    	List<? extends IReqCacheEntry> result = bcManager.getReqsCache(authentication, detailed == null ? false : detailed);
        return new ResponseEntity<>(result, new HttpHeaders(), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/traces/cache/{id}", method = RequestMethod.DELETE, produces = {"application/json"})
    public ResponseEntity<?> deleteTracesCache(Authentication authentication,
    		@PathVariable("id") UUID id) 
    		throws UnsupportedDataTypeException {
    	IReqCacheEntry result = bcManager.deleteReqCache(authentication, id);
        return new ResponseEntity<>(result, new HttpHeaders(), HttpStatus.OK);
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
    
    /**
     * Search the blockchain(s) for traces with the valid values given the search parameters.
     * Every parameter can appear multiple times, in which case all traces with a value found in the
     *     set comprising all values of a given parameter will be considered valid.        
     *  Be aware that this call will only search through a blockchain and return successfully
     *     incorporated traces.
     *     
     * @param authentication
     * @param usersIds the IDs of the users that executed the action for which a trace has been generated
     * @param callerUsersIds the IDs of the users that requested the creation of a trace
     * @param datasetsIds the IDs of the datasets
     * @param userActions the user actions for which traces have been created
     * @param blockchains the blockchains that will be searched
     * @return
     * @throws BadRequest
     * @throws UnsupportedDataTypeException
     * @throws MissingServletRequestParameterException
     */
    @RequestMapping(value = "/traces", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<?> getTraces(Authentication authentication, 
    		@RequestParam(name = "userId", required=false) List<String> usersIds,
    		@RequestParam(name = "callerUserId", required=false) List<String> callerUsersIds,
    		@RequestParam(name = "datasetId", required=false) List<String> datasetsIds,
    		@RequestParam(name = "userAction", required=false) List<UserAction> userActions,
    		@RequestParam(name = "blockchain", required=false) Set<BlockchainType> blockchains
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
    	fp.setBlockchains(blockchains);
    	fp.setCallerUsersIds(callerUsersIds);
    	fp.setDatasetsIds(datasetsIds);
    	fp.setUsersIds(usersIds);
    	fp.setUserActions(userActions);
        return new ResponseEntity<>(bcManager.getTraces(fp), new HttpHeaders(), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/traces/{traceId}", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<?> getTrace(Authentication authentication,
    		@PathVariable("traceId") String traceId) throws BadRequest, UnsupportedDataTypeException, MissingServletRequestParameterException {
        return new ResponseEntity<>(bcManager.getTraceById(traceId), new HttpHeaders(), HttpStatus.OK);
    }
    
//    @RequestMapping(value = "/traces/{userId}/{actionIdName}", method = RequestMethod.GET, produces = {"application/json"})
//    public ResponseEntity<?> getLogsByUserAction(@PathVariable(name = "userId", required = true) String userId,
//    		@PathVariable(name = "actionIdName", required = true) String actionIdName) {
//        return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.OK);
//    }
    
    @RequestMapping(value = "/static/traces/actions", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<?> getActions(Authentication authentication) {
    	
        return new ResponseEntity<>(UserAction.values(), new HttpHeaders(), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/static/traces/hashes", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<?> getHashTypes(Authentication authentication) {
        return new ResponseEntity<>(HashType.values(), new HttpHeaders(), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/static/traces/dataset_resources", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<?> getDatasetResources(Authentication authentication) {
        return new ResponseEntity<>(DatasetResourceType.values(), new HttpHeaders(), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/static/traces/request_resource_contents", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<?> getRequestResourceContentTypes(Authentication authentication) {
        return new ResponseEntity<>(ReqResContentType.values(), new HttpHeaders(), HttpStatus.OK);
    }

	
}
