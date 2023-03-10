package es.upv.grycap.tracer.model;

import org.springframework.security.core.GrantedAuthority;

public enum TracerRoles  implements GrantedAuthority {
	
	admin, trace_writer;
	
	public static final String PREFIX = "";//"ROLE_";
	
	public String toRole() {
		return PREFIX + name();
	}

    @Override
    public String getAuthority() {
        return toRole();
    }

}
