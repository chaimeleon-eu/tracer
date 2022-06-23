package es.upv.grycap.tracer.service;

import java.util.Set;
import java.util.stream.Collectors;

import javax.activation.UnsupportedDataTypeException;

import org.apache.commons.codec.binary.Hex;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import es.upv.grycap.tracer.exceptions.UncheckedExceptionFactory;
import es.upv.grycap.tracer.exceptions.UncheckedUnsupportedDataTypeException;
import es.upv.grycap.tracer.model.TracerRoles;
import es.upv.grycap.tracer.model.dto.HashType;

/**
*
* @author Andy S Alic (asalic)
*/
@Service
public class UserManager {
	
	protected HashingService hashingService;
	
	@Autowired
	public UserManager(@Autowired HashingService hashingService) {
		this.hashingService = hashingService;
	}

    
    public Set<String> getAuthenticatedUserRoles(Authentication authentication) {
    	Object principal = authentication.getPrincipal();
    	if (principal instanceof User) {
    		User user = (User) principal;
    		return user.getAuthorities().stream().map(authority -> authority.getAuthority()).collect(Collectors.toSet());
    	} else if (principal instanceof KeycloakPrincipal) {
    		KeycloakPrincipal<KeycloakSecurityContext> pr = (KeycloakPrincipal<KeycloakSecurityContext>)principal;
    		Set<String> roleNames = pr.getKeycloakSecurityContext().getToken().getRealmAccess().getRoles();
    		return roleNames;
    	} else {
    		throw new UncheckedUnsupportedDataTypeException("The user authentication type is unsupported");
    	}
    }
    
    public String getUserId(Authentication authentication)  {
    	Object principal = authentication.getPrincipal();
    	if (principal instanceof User) {
    		User user = (User) principal;
    		return user.getUsername();
    	} else if (principal instanceof KeycloakPrincipal) {
    		KeycloakPrincipal<KeycloakSecurityContext> pr = (KeycloakPrincipal<KeycloakSecurityContext>)principal;
    		return  pr.getKeycloakSecurityContext().getIdToken().getSubject();
    	} else {
    		throw new UncheckedUnsupportedDataTypeException("The user authentication type is unsupported");
    	}
    }
    
    public boolean userHasRole(final Authentication authentication, TracerRoles role) {
    	Object principal = authentication.getPrincipal();
    	if (principal instanceof User) {
    		User user = (User) principal;
    		return user.getAuthorities().stream().map(authority -> authority.getAuthority()).collect(Collectors.toSet()).contains("ROLE_" + role.name());
    	} else if (principal instanceof KeycloakPrincipal) {
    		KeycloakPrincipal<KeycloakSecurityContext> pr = (KeycloakPrincipal<KeycloakSecurityContext>)principal;
    		return pr.getKeycloakSecurityContext().getToken().getRealmAccess().getRoles().contains("ROLE_" + role.name());
    	} else {
    		throw new UncheckedUnsupportedDataTypeException("The user authentication type is unsupported");
    	}
    }

    public String getCallerUserId(Authentication authentication){
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
    		throw new UncheckedUnsupportedDataTypeException("The user authentication type is unsupported");
    	}
    }
    
    public boolean isAdmin(Authentication authentication) {
    		return getAuthenticatedUserRoles(authentication).contains("ROLE_" + TracerRoles.TRACER_ADMIN);
    }
    
}
