package es.upv.grycap.tracer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import es.upv.grycap.tracer.model.FilterParams;
import es.upv.grycap.tracer.model.trace.TraceBase;
import es.upv.grycap.tracer.model.trace.v1.Trace;
import es.upv.grycap.tracer.model.trace.v1.TraceDataset;
import es.upv.grycap.tracer.model.trace.v1.TraceUseDatasets;

@Service
public class TraceFiltering {
	
	public List<TraceBase> filterTraces(List<Trace> traces, final FilterParams fp) {
		return traces.stream().filter(t -> {
			List<Boolean> filters = new ArrayList<>();
			if (fp.hasCallerUsersIds()) {
				TraceDataset td = (TraceDataset) t;
				filters.add(fp.containsCallerUserId(td.getCallerId()));
			}
			if (fp.hasUsersIds()) {
				TraceDataset td = (TraceDataset) t;
				filters.add(fp.containsUserId(td.getUserId()));
			}
			if (fp.hasUserActions()) {
				TraceDataset td = (TraceDataset) t;
				filters.add(fp.containsUserAction(td.getUserAction()));
			}
			if (fp.hasDatasetsIds()) {
				if (t instanceof TraceDataset) {
					TraceDataset td = (TraceDataset) t;
					filters.add(fp.containsDatasetId(td.getDatasetId()));								
				} else if (t instanceof TraceUseDatasets) {
					TraceUseDatasets td = (TraceUseDatasets) t;
					boolean foundId = false;
					for (String id: td.getDatasetsIds()) {
						if (fp.containsDatasetId(id)) {
							foundId = true;
							break;
						}										
					}
					filters.add(foundId);
				} else {
					filters.add(false);
				}
			}
			return !filters.contains(false);
		}).collect(Collectors.toList());
	}

}
