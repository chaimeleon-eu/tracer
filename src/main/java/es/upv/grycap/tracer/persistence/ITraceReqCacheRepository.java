package es.upv.grycap.tracer.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.upv.grycap.tracer.model.trace.TraceReqCacheEntry;



@Repository
public interface ITraceReqCacheRepository extends JpaRepository<TraceReqCacheEntry, UUID> {

	@Query("select p.id from #{#entityName} p")
	public List<String> getAllTransactionsIds();

}
