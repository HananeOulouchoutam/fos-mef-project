package ma.twins.project_service.dtos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.twins.project_service.entities.Phase;
import ma.twins.project_service.model.Client;
import ma.twins.project_service.model.Employee;


@Data @NoArgsConstructor @AllArgsConstructor 
public class ProjectResponse {
	private Long id;
	private String title;
	private String description;
	private LocalDate startDate;
	private int duration;
	private Client client;
	private int progress;
	private double totalBudget ;
	private List<Phase> phases = new ArrayList<>();
	private List<Employee> team = new ArrayList<>();
}
