package ma.twins.leave_service.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ma.twins.leave_service.entities.Leave;
import ma.twins.leave_service.enums.LeaveStatus;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
	@Query("SELECT l FROM Leave l ORDER BY CASE WHEN l.status = :pending THEN 0 ELSE 1 END, l.startDate DESC")
	Page<Leave> findAllOrderByPendingFirst(@Param("pending") LeaveStatus pending, Pageable pageable);
	Page<Leave> findByStatus(LeaveStatus status, Pageable pageable);
	
//	
//	List<Leave> findByStatus(LeaveStatus status);
//	Page<Leave> findByStatus(LeaveStatus status, Pageable pageable);

}
