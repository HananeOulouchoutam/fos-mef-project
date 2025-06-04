package ma.twins.project_service.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.twins.project_service.enums.Status;
import ma.twins.project_service.model.Client;
import ma.twins.project_service.model.Employee;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String description;
	private LocalDate startDate;
	private int duration;
	private LocalDate updatedDate ;
	private Long clientId;
	@Transient
	private Client client;
	private int progress;
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Phase> phases = new ArrayList<>();
	private List<Long> teamIds = new ArrayList<>();
	@Transient
	private List<Employee> team = new ArrayList<>();
	private double totalBudget ;
	
	public int calculateProgress() {
	    if (phases == null || phases.isEmpty()) {
	        return 0;
	    }

	    long completedCount = phases.stream()
	        .filter(phase -> phase.getStatus() == Status.COMPLETED)
	        .count();

	    return (int) ((completedCount * 100.0) / phases.size());
	}
	
	public double calculateTotalBudget() {
	    if (phases == null || phases.isEmpty()) {
	        return 0;
	    }

	    return phases.stream()
	                 .mapToDouble(Phase::getBudget)
	                 .sum();
	}

}
