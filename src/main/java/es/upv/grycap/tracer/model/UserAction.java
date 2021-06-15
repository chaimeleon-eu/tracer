package es.upv.grycap.tracer.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserAction {
	
	CREATE_NEW_DATASET(1, "Create new dataset"),
	CREATE_NEW_VERSION_DATASET(2, "Create new version of existing dataset"),
	VISUALIZE_VERSION_DATASET(3, "Visualize a version of dataset"),
	USE_DATASET_POD(4, "Use a dataset in a Kubernetes App"),
	CREATE_MODEL_POD(5, "Create a new model in a Kubernetes App"),
	USE_MODEL_POD(6, "Use an existing model in a Kubernetes App");
	
	public final int id;
	public final String name;
	public final String description;
	
	@JsonCreator 
	private UserAction(int id, String description) {
        this.id = id;
        this.description = description;
        this.name = name();
    }
	
	@Override
	public String toString() {
		return name;
	}

}
