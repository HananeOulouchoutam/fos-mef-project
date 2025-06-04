package ma.twins.leave_service.services;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import ma.twins.leave_service.model.Employee;

@FeignClient(name="EMPLOYEE-SERVICE")
public interface EmployeeRestClient {

	@GetMapping("/employees/{id}?projection=leaveEmployee")
	public Employee customerById(@PathVariable Long id );
	@GetMapping("/api/employees/search")
	List<Employee> getEmployeeByName(
	        @RequestParam(name = "keyword", required = false) String keyword,
	        @RequestParam(name = "position", required = false) String position
	 );
		

}
