package es.upv.grycap.tracer.model;

public enum DatasetAction {
	
	CREATE_NEW_DS((byte)1, "Create new dataset"),
	CREATE_NEW_VERSION_EXISTING_DS((byte)2, "Create new version of existing dataset"),
	VISUALIZE_VERSION_DS((byte)3, "Visualize a version of dataset");
	
	public final byte id;
	public final String label;
	
	private DatasetAction(byte id, String label) {
        this.id = id;
        this.label = label;
    }

}
