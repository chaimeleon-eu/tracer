package es.upv.grycap.tracer.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserAction {
	
	CREATE_NEW_DS(1, "Create new dataset"),
	CREATE_NEW_VERSION_EXISTING_DS(2, "Create new version of existing dataset"),
	VISUALIZE_VERSION_DS(3, "Visualize a version of dataset");
	
	public final int id;
	public final String name;
	public final String description;
	
	@JsonCreator 
	private UserAction(int id, String description) {
        this.id = id;
        this.description = description;
        this.name = name();
    }

}
