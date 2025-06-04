package ma.twins.project_service.dtos;

import java.time.LocalDate;
import lombok.Data;
import ma.twins.project_service.enums.PhaseType;
import ma.twins.project_service.enums.Status;




@Data
public class PhaseRequest {
	private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private PhaseType type ;
    private Long projectId; 
    private Status status ;
    private double budget ;

}
