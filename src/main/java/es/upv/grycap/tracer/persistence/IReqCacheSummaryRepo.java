package es.upv.grycap.tracer.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.upv.grycap.tracer.model.IReqCacheEntry;
import es.upv.grycap.tracer.model.ReqCacheEntryDetailed;
import es.upv.grycap.tracer.model.ReqCacheEntrySummary;
import es.upv.grycap.tracer.model.dto.ReqCacheStatus;
import es.upv.grycap.tracer.model.trace.TraceReqCacheEntry;



@Repository
public interface IReqCacheSummaryRepo extends JpaRepository<ReqCacheEntrySummary, UUID> {

	@Query("select p.id from #{#entityName} p")
	public List<String> getAllTransactionsIds();
	


}
