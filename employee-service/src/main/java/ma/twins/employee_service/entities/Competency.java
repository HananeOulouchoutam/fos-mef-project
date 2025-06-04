package ma.twins.employee_service.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.twins.employee_service.enums.ProficiencyLevel;



@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Competency {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id ;
   private String skillName ;
   private ProficiencyLevel proficiencyLevel ;
   private int yearsOfExperience ;
   
   @ManyToOne()
   @JsonProperty(access= Access.WRITE_ONLY)
   private Employee employee ;
}
