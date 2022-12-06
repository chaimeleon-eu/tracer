package es.upv.grycap.tracer.model.trace;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TracesFilteredPagination {
    
    /**
     * The sub-list of filtered traces, extracted from the list of all filtered traces
     * between the skip and skip + limit margins
     */
    protected List<TraceSummaryBase> traces;
    
    /**
     * Count of all traces after applying the filters
     */
    protected Integer countAllTraces;

}
