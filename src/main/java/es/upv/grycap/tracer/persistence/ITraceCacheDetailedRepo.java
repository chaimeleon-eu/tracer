package es.upv.grycap.tracer.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.upv.grycap.tracer.model.TraceCacheDetailed;
import es.upv.grycap.tracer.model.dto.ReqCacheStatus;

@Repository
public interface ITraceCacheDetailedRepo extends JpaRepository<TraceCacheDetailed, UUID> {
	
	@Query("SELECT r FROM #{#entityName} r WHERE r.status IN :statuses")
	public List<TraceCacheDetailed> findAllByStatuses(@Param("statuses") List<ReqCacheStatus> statuses);

}
