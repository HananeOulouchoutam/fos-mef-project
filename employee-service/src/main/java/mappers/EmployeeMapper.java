package mappers;

import dtos.EmployeeResponse;
import ma.twins.employee_service.entities.Employee;

public class EmployeeMapper {
	
	 public static EmployeeResponse mapToResponse(Employee e) {
		 
	        EmployeeResponse res = new EmployeeResponse();
	        res.setId(e.getId());
	        res.setFirstName(e.getFirstName());
	        res.setLastName(e.getLastName());
	        res.setEmail(e.getEmail());
	        res.setCnss(e.getCnss());
	        res.setPhoneNumber(e.getPhoneNumber());
	        res.setPosition(e.getPosition());
	        res.setCreatedAt(e.getCreatedAt());
	        res.setStatus(e.getStatus());
	        res.setUsedVacationDays(e.getUsedVacationDays());
	        res.setCompetencies(e.getCompetencies());
	        res.setImageUrl(e.getImageUrl());
	        return res;
	    }
}
