package es.upv.grycap.tracer.model;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import es.upv.grycap.tracer.model.trace.v1.UserAction;
import lombok.Getter;

@Getter
public class FilterParams implements IFilterParams {
	
	protected Set<String> usersIds;
	protected Set<String> callerUsersIds;
	protected Set<String> datasetsIds;
	protected Set<UserAction> userActions;
	
//	public FilterParams() {
//		usersIds = Set.of();
//		callerUsersIds = Set.of();
//		datasetsIds = Set.of();
//	}
	
	public void setUsersIds(final Collection<String> usersIds) {
		this.usersIds = usersIds.stream().map(e -> e.toLowerCase()).collect(Collectors.toSet());
	}
	
	public void setCallerUsersIds(final Collection<String> callerUsersIds) {
		this.callerUsersIds = callerUsersIds.stream().map(e -> e.toLowerCase()).collect(Collectors.toSet());
	}
	
	public void setDatasetsIds(final Collection<String> datasetsIds) {
		this.datasetsIds = datasetsIds.stream().map(e -> e.toLowerCase()).collect(Collectors.toSet());
	}
	
	public void setUserActions(final Collection<UserAction> userActions) {
		this.userActions = userActions.stream().collect(Collectors.toSet());
	}

	public boolean hasUsersIds() {return usersIds != null && !usersIds.isEmpty();}
	public boolean containsUserId(String userId) {return hasUsersIds() && usersIds.contains(userId.toLowerCase());}
	public boolean hasCallerUsersIds() {return callerUsersIds != null && !callerUsersIds.isEmpty();}
	public boolean containsCallerUserId(String callerUserId) {return hasCallerUsersIds() && callerUsersIds.contains(callerUserId.toLowerCase());}
	public boolean hasDatasetsIds() {return datasetsIds != null && !datasetsIds.isEmpty();}
	public boolean containsDatasetId(String datasetId) {return hasDatasetsIds() && datasetsIds.contains(datasetId.toLowerCase());}
	public boolean hasUserActions() {return userActions != null && !userActions.isEmpty();}
	public boolean containsUserAction(UserAction userAction) {return hasUserActions() && userActions.contains(userAction);}

}
