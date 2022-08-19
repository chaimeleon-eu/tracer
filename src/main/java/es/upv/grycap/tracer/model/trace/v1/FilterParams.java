package es.upv.grycap.tracer.model.trace.v1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import es.upv.grycap.tracer.model.IFilterParams;
import es.upv.grycap.tracer.model.dto.BlockchainType;
import lombok.Getter;

@Getter
public class FilterParams implements IFilterParams {
	
	protected Set<BlockchainType> blockchains;
	protected Set<String> usersIds;
	protected Set<String> callerUsersIds;
	protected Set<String> datasetsIds;
	protected Set<UserAction> userActions;
	
//	public FilterParams() {
//		usersIds = Set.of();
//		callerUsersIds = Set.of();
//		datasetsIds = Set.of();
//	}
	
	public void setBlockchains(final Collection<BlockchainType> blockchains) {
		if (blockchains != null)
			this.blockchains = blockchains.stream().collect(Collectors.toSet());
	}
	
	public void setUsersIds(final Collection<String> usersIds) {
		if (usersIds != null)
			this.usersIds = usersIds.stream().map(e -> e.toLowerCase()).collect(Collectors.toSet());
	}
	
	public void setCallerUsersIds(final Collection<String> callerUsersIds) {
		if (callerUsersIds != null)
			this.callerUsersIds = callerUsersIds.stream().map(e -> e.toLowerCase()).collect(Collectors.toSet());
	}
	
	public void setDatasetsIds(final Collection<String> datasetsIds) {
		if (datasetsIds != null)
			this.datasetsIds = datasetsIds.stream().map(e -> e.toLowerCase()).collect(Collectors.toSet());
	}
	
	public void setUserActions(final Collection<UserAction> userActions) {
		if (userActions != null)
			this.userActions = userActions.stream().collect(Collectors.toSet());
	}

	public boolean hasBlockchains() {return blockchains != null && !blockchains.isEmpty();}
	public boolean containsBlockchain(BlockchainType blockchain) {return hasBlockchains() && blockchains.contains(blockchain);}
	public boolean hasUsersIds() {return usersIds != null && !usersIds.isEmpty();}
	public boolean containsUserId(String userId) {return hasUsersIds() && usersIds.contains(userId.toLowerCase());}
	public boolean hasCallerUsersIds() {return callerUsersIds != null && !callerUsersIds.isEmpty();}
	public boolean containsCallerUserId(String callerUserId) {return hasCallerUsersIds() && callerUsersIds.contains(callerUserId.toLowerCase());}
	public boolean hasDatasetsIds() {return datasetsIds != null && !datasetsIds.isEmpty();}
	public boolean containsDatasetId(String datasetId) {return hasDatasetsIds() && datasetsIds.contains(datasetId.toLowerCase());}
	public boolean hasUserActions() {return userActions != null && !userActions.isEmpty();}
	public boolean containsUserAction(UserAction userAction) {return hasUserActions() && userActions.contains(userAction);}
	
	public Collection<String> toCollectionOfValues() {
		Set<String> result = new HashSet<>();
		if (hasBlockchains()) {
			blockchains.forEach(bc -> result.add(bc.name()));
		}
		if (hasCallerUsersIds()) {
			result.addAll(callerUsersIds);
		}
		if (hasDatasetsIds()) {
			result.addAll(datasetsIds);
		}
		if (hasUserActions()) {
			userActions.forEach(ua -> result.add(ua.name()));
		}
		if (hasUsersIds()) {
			result.addAll(usersIds);
		}
		return result;
	}

}
