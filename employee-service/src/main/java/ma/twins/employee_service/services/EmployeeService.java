package ma.twins.employee_service.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dtos.EmployeeRequest;
import dtos.EmployeeResponse;
import ma.twins.employee_service.enums.Position;

public interface EmployeeService {
   
    EmployeeResponse addEmployee(EmployeeRequest request);
    Page<EmployeeResponse> getEmployees(Pageable pageable);
    List<EmployeeResponse> searchEmployees(Position position, String keyword);
    long countEmployees();
	
	

}
