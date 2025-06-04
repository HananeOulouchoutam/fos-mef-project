package ma.twins.leave_service.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ma.twins.leave_service.entities.Leave;
import ma.twins.leave_service.enums.LeaveStatus;

public interface LeaveService {
	Page<Leave> getLeavesOrderedByStatus(Pageable pageable);
	Leave updateLeave(Long id, Leave leave);
	Leave updateLeaveStatus(Long id, LeaveStatus status);
	Page<Leave> searchLeavesByEmployeeName(String name, Pageable pageable);
}
