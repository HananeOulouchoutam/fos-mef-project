package ma.twins.leave_service.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import ma.twins.leave_service.entities.Leave;
import ma.twins.leave_service.enums.LeaveStatus;
import ma.twins.leave_service.services.LeaveService;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {

	 private final LeaveService leaveService;

	    @GetMapping
	    @PreAuthorize("hasAuthority('RH')")
	    public Page<Leave> getLeavesOrderedByStatus(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "5") int size
	    ) {
	        Pageable pageable = PageRequest.of(page, size);
	        return leaveService.getLeavesOrderedByStatus(pageable);
	    }

	    @GetMapping("/search")
	    @PreAuthorize("hasAuthority('RH')")
	    public Page<Leave> searchLeaves(
	        @RequestParam(required = false) String name,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "5") int size
	    ) {
	        Pageable pageable = PageRequest.of(page, size);
	        return leaveService.searchLeavesByEmployeeName(name, pageable);
	    }

	   

	 
	    @PutMapping("/{id}/status")
	    @PreAuthorize("hasAuthority('RH')")
	    public ResponseEntity<Leave> updateLeaveStatus(@PathVariable Long id, @RequestParam LeaveStatus status) {
	        try {
	            Leave updatedLeave = leaveService.updateLeaveStatus(id, status);
	            return new ResponseEntity<>(updatedLeave, HttpStatus.OK);
	        } catch (RuntimeException e) {
	            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	        }
	    }

}
