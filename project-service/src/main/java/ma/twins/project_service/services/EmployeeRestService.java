package ma.twins.project_service.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ma.twins.project_service.model.Employee;



@FeignClient(name="EMPLOYEE-SERVICE")
public interface EmployeeRestService {
	@GetMapping("/employees/{id}?projection=leaveEmployee")
	public Employee customerById(@PathVariable Long id );
}
