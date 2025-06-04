package ma.twins.leave_service.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.twins.leave_service.enums.LeaveStatus;
import ma.twins.leave_service.enums.LeaveType;
import ma.twins.leave_service.model.Employee;


@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Leave {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id ;
   private Long employeeId ;
   @Transient
   private Employee employee ;
   private LocalDate startDate ;
   private LocalDate endDate ;
   private int daysRequested ;
   @Enumerated(EnumType.STRING)
   private LeaveType type ;
   private String reason ;
   private LocalDate dateRequested ;
   private LeaveStatus status ;
   private boolean abroad;
   private String country;
}
