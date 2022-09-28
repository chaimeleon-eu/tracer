package es.upv.grycap.tracer.model;

public enum TracerRoles {
	
	ADMIN, TRACE_WRITER;
	
	public static final String PREFIX = "ROLE_";
	
	public String toRole() {
		return PREFIX + name();
	}

}
