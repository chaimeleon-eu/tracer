package es.upv.grycap.tracer.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum UserAction {
	@JsonProperty("CREATE_DATASET")
	CREATE_DATASET(1, "Create a new dataset"),
	@JsonProperty("CREATE_VERSION_DATASET")
	CREATE_VERSION_DATASET(2, "Create new version of existing dataset"),
	@JsonProperty("VISUALIZE_VERSION_DATASET")
	VISUALIZE_VERSION_DATASET(3, "Visualize a version of dataset"),
	@JsonProperty("USE_DATASET_POD")
	USE_DATASET_POD(4, "Use a dataset in a Kubernetes App"),
	@JsonProperty("CREATE_MODEL_POD")
	CREATE_MODEL_POD(5, "Create a new model in a Kubernetes App"),
	@JsonProperty("USE_MODEL_POD")
	USE_MODEL_POD(6, "Use an existing model in a Kubernetes App");
	
	@JsonIgnore
	public final int id;
	@JsonIgnore
	public final String description;
	
	private UserAction(int id, String description) {
        this.id = id;
        this.description = description;
    }
	
	@Override
	public String toString() {
		return name();
	}

}
