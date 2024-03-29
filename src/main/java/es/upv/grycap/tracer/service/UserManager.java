package es.upv.grycap.tracer.service;

import java.util.Set;
import java.util.stream.Collectors;

import javax.activation.UnsupportedDataTypeException;

import org.apache.commons.codec.binary.Hex;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken.Access;
import org.keycloak.representations.IDToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import es.upv.grycap.tracer.exceptions.UncheckedExceptionFactory;
import es.upv.grycap.tracer.exceptions.UncheckedUnsupportedDataTypeException;
import es.upv.grycap.tracer.model.TracerRoles;
import es.upv.grycap.tracer.model.dto.HashType;
import lombok.extern.slf4j.Slf4j;

/**
*
* @author Andy S Alic (asalic)
*/
@Service
@Slf4j
public class UserManager {
    
    protected String clientId;
    
    public UserManager(@Value("${keycloak.resource}") String clientId) {
        this.clientId = clientId;
    }


    
//    public Set<String> getAuthenticatedUserRoles(Authentication authentication) {
//    	Object principal = authentication.getPrincipal();
//    	if (principal instanceof User) {
//    		User user = (User) principal;
//    		return user.getAuthorities().stream().map(authority -> authority.getAuthority()).collect(Collectors.toSet());
//    	} else if (principal instanceof KeycloakPrincipal) {
//    		KeycloakPrincipal<KeycloakSecurityContext> pr = (KeycloakPrincipal<KeycloakSecurityContext>)principal;
//    		Set<String> roleNames = pr.getKeycloakSecurityContext().getToken().getRealmAccess().getRoles();
//    		SecurityContextHolder.getContext().getAuthentication();
//    		pr.getKeycloakSecurityContext().getToken().getResourceAccess().entrySet()
//    		        .forEach(e -> {
//    		            log.info(e.getKey());
//                        log.info(e.getValue().getRoles().stream().collect(Collectors.joining(", ")));
//    		            roleNames.addAll(e.getValue().getRoles());
//    		        });
//    		return roleNames;
//    	} else {
//    		throw new UncheckedUnsupportedDataTypeException("The user authentication type is unsupported");
//    	}
//    }
    
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
    		String id = Hex.encodeHexString(HashingService.getHash(user.getUsername().getBytes(), HashType.SHA3_512).getHash());
    		return id;
    	} else if (principal instanceof KeycloakPrincipal) {
    		KeycloakPrincipal<KeycloakSecurityContext> pr = (KeycloakPrincipal<KeycloakSecurityContext>)principal;
    		//log.info("User caller subject is: " + pr.getKeycloakSecurityContext().getToken().getSubject());
    		//log.info("User caller ID is: " + pr.getKeycloakSecurityContext().getToken().getId());

    		return pr.getKeycloakSecurityContext().getToken().getSubject();
    	} else {
    		throw new UncheckedUnsupportedDataTypeException("The user authentication type is unsupported");
    	}
    }
    
//    public boolean isAdmin(Authentication authentication) {
//        log.info(getAuthenticatedUserRoles(authentication).stream().collect(Collectors.joining(", ")));
//    		return getAuthenticatedUserRoles(authentication).contains(TracerRoles.admin.toRole());
//    }
    
    public boolean isAdmin(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            return user.getAuthorities().stream().map(authority -> authority.getAuthority()).collect(Collectors.toSet()).contains(TracerRoles.admin.toRole());
        } else if (principal instanceof KeycloakPrincipal) {
            KeycloakPrincipal<KeycloakSecurityContext> pr = (KeycloakPrincipal<KeycloakSecurityContext>)principal;
            //Set<String> roleNames = pr.getKeycloakSecurityContext().getToken().getRealmAccess().getRoles();
            
            Access a = pr.getKeycloakSecurityContext().getToken().getResourceAccess().get(clientId);
            if (a == null) {
                return false;
            } else {
                return a.getRoles().contains(TracerRoles.admin.toRole());
            }
        } else {
            throw new UncheckedUnsupportedDataTypeException("The user authentication type is unsupported");
        }
    }
    
}
