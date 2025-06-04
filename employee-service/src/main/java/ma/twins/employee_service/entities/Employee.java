package ma.twins.employee_service.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.twins.employee_service.enums.AccountStatus;
import ma.twins.employee_service.enums.Position;



@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder 
public class Employee {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id ;
   private String firstName ;
   private String lastName ;
   private String cnss ;
   private String cin ;
   private String email ;
   private String phoneNumber ;
   private String imageUrl;
   private Position position;
   private LocalDate createdAt ;
   private LocalDate updatedAt ;
   private LocalDate departureDate;
   private AccountStatus status ;
   private int vacationDaysPerYear  ;
   private int usedVacationDays ;
   @OneToMany(mappedBy = "employee")
   private List<Competency> competencies ;
   
}
   
