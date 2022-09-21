package es.upv.grycap.tracer.model.trace.v1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import es.upv.grycap.tracer.exceptions.UnhandledException;
import es.upv.grycap.tracer.model.IFilterParams;
import es.upv.grycap.tracer.model.dto.BlockchainType;
import es.upv.grycap.tracer.model.trace.TraceBase;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
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
	
	public Collection<TraceBase> filterTraces(BlockchainType btype, final Collection<TraceBase> traces) {

		if (hasBlockchains()) {
			if (!blockchains.contains(btype))
				return List.of();
		}
		Collection<TraceBase> result = new ArrayList<>();
		for (final TraceBase t: traces) {
			switch (t.getVersion()) {
			case V1: 
				if (filterTracesV1(t))
					result.add(t);
				break;
			default: throw new UnhandledException("Trace version " + t.getVersion().name() + " not handled");
			}
		}
		return result;
	}
	
	public boolean filterTracesV1( TraceBase trace) {
		boolean result = true;
		if (hasCallerUsersIds()) {
			Trace t = (Trace) trace;
			result &= callerUsersIds.contains(t.getCallerId());
		}
		if (hasDatasetsIds()) {
			if (trace instanceof TraceDataset) {
				log.info("Trace is TraceDataset");
				TraceDataset t = (TraceDataset) trace;
				result &= datasetsIds.contains(t.getDatasetId());
			} else if (trace instanceof TraceUseDatasets) {
				log.info("Trace is TraceUseDatasets");
				TraceUseDatasets t = (TraceUseDatasets) trace;
				Set<String> dsIds = new HashSet<>(t.getDatasetsIds());
				dsIds.retainAll(datasetsIds);
				result &= !dsIds.isEmpty();
			} else if (trace instanceof TraceCreateModel) {
				log.info("Trace is TraceCreateModel");
				TraceCreateModel t = (TraceCreateModel) trace;
				Set<String> dsIds = new HashSet<>(t.getDatasetsIds());
				dsIds.retainAll(datasetsIds);
				result &= !dsIds.isEmpty();
			}
		}
		if (hasUserActions()) {
			Trace t = (Trace) trace;
			result &= userActions.contains(t.getUserAction());
		}
		if (hasUsersIds()) {
			Trace t = (Trace) trace;
			result &= usersIds.contains(t.getUserId());
		}
		return result;
	}

}
