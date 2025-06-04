package dtos;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import ma.twins.employee_service.entities.Competency;
import ma.twins.employee_service.enums.AccountStatus;
import ma.twins.employee_service.enums.Position;

@Data
public class EmployeeResponse {
	private Long id;
	private String firstName;
	private String lastName;
	private String cnss;
	private String email;
	private String phoneNumber;
	private String imageUrl;
	private Position position;
	private LocalDate createdAt;
	private AccountStatus status;
	private int usedVacationDays;
	private List<Competency> competencies;
}
