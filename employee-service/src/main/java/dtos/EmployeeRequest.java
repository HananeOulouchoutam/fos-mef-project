package dtos;

import lombok.Data;
import ma.twins.employee_service.enums.Position;

@Data
public class EmployeeRequest {
	private Long id;
	private String firstName;
	private String lastName;
	private String cnss;
	private String email;
	private String phoneNumber;
	private Position position;
}
