package es.upv.grycap.tracer.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.upv.grycap.tracer.model.trace.v1.TraceCacheEntry;


@Repository
public interface ITraceCacheRepository extends JpaRepository<TraceCacheEntry, Long> {

	@Query("select p.id from #{#entityName} p")
	public List<String> getAllTransactionsIds();

}
