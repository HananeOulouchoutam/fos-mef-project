package ma.twins.employee_service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ma.twins.employee_service.entities.Competency;
import ma.twins.employee_service.entities.Employee;
import ma.twins.employee_service.enums.AccountStatus;
import ma.twins.employee_service.enums.Position;
import ma.twins.employee_service.enums.ProficiencyLevel;
import ma.twins.employee_service.repositories.CompetencyRepository;
import ma.twins.employee_service.repositories.EmployeeRepository;

@SpringBootApplication
public class EmployeeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeServiceApplication.class, args);
	}
	
	@Bean
	CommandLineRunner start(EmployeeRepository employeeRepository, CompetencyRepository competencyRepository) {
	    return args -> {
	        List<Employee> employees = new ArrayList<>();

	        Employee emp1 = Employee.builder()
	                .firstName("Hanane")
	                .lastName("Oulouchoutam")
	                .position(Position.PROJECT_MANAGER)
	                .cnss("CNSS100001")
	                .email("oulouchoutamhanane@gmail.com")
	                .phoneNumber("060000001")
	                .createdAt(LocalDate.now())
	                .updatedAt(LocalDate.now())
	                .status(AccountStatus.ACTIVE)
	                .vacationDaysPerYear(25)
	                .usedVacationDays(5)
	                .imageUrl("pr_hananeoulouchoutam.jpg")
	                .build();

	        Employee emp2 = Employee.builder()
	                .firstName("Mohamed")
	                .lastName("Oulouchoutam")
	                .position(Position.DESIGNER)
	                .cnss("CNSS100002")
	                .email("oulouchoutamhanane@gmail.com")
	                .phoneNumber("060000002")
	                .createdAt(LocalDate.now())
	                .updatedAt(LocalDate.now())
	                .status(AccountStatus.ACTIVE)
	                .vacationDaysPerYear(22)
	                .usedVacationDays(3)
	                .imageUrl("pr_mohamedoulouchoutam.jpg")
	                .build();

	        Employee emp3 = Employee.builder()
	                .firstName("Laila")
	                .lastName("oulouchoutam")
	                .position(Position.DEVELOPER)
	                .cnss("CNSS100003")
	                .email("oulouchoutamhanane@gmail.com")
	                .phoneNumber("060000003")
	                .createdAt(LocalDate.now())
	                .updatedAt(LocalDate.now())
	                .status(AccountStatus.ACTIVE)
	                .vacationDaysPerYear(20)
	                .usedVacationDays(8)
	                .imageUrl("pr_lailaoulouchoutam.jpg")
	                .build();

	 

	       

	        employees.addAll(List.of(emp1, emp2, emp3));

	        employeeRepository.saveAll(employees);

	        List<Competency> competencies = new ArrayList<>();

	        for (Employee emp : employees) {
	            Competency comp1 = Competency.builder()
	                    .skillName("Skill" + emp.getFirstName())
	                    .proficiencyLevel(ProficiencyLevel.EXPERT)
	                    .yearsOfExperience(3)
	                    .employee(emp)
	                    .build();

	            Competency comp2 = Competency.builder()
	                    .skillName("Tool" + emp.getLastName())
	                    .proficiencyLevel(ProficiencyLevel.INTERMEDIATE)
	                    .yearsOfExperience(2)
	                    .employee(emp)
	                    .build();

	            competencies.addAll(List.of(comp1, comp2));
	        }

	        competencyRepository.saveAll(competencies);

	        System.out.println("6 test employees created with their competencies.");
	    };
	}

	@Bean
	CommandLineRunner createUploadDir() {
	    return args -> {
	        Path uploadPath = Paths.get("uploads/profiles");
	        if (!Files.exists(uploadPath)) {
	            Files.createDirectories(uploadPath);
	            System.out.println("Dossier uploads/employees créé !");
	        } else {
	            System.out.println("Dossier uploads/employees existe déjà.");
	        }
	    };
	}



}
