package ma.twins.leave_service;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import ma.twins.leave_service.entities.Leave;
import ma.twins.leave_service.enums.LeaveStatus;
import ma.twins.leave_service.enums.LeaveType;
import ma.twins.leave_service.repositories.LeaveRepository;

@SpringBootApplication
@EnableFeignClients
public class LeaveServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeaveServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner initLeaves(LeaveRepository leaveRepository) {
		return args -> {
			Leave leave1 = Leave.builder()
				    .employeeId(1L)
				    .startDate(LocalDate.of(2025, 6, 1))
				    .endDate(LocalDate.of(2025, 6, 5))
				    .daysRequested(5)
				    .type(LeaveType.SICK)
				    .reason("Je souhaite prendre quelques jours de repos afin de me ressourcer auprès de ma famille. Cette pause me permettra de revenir avec un esprit plus clair, plus concentré, et une énergie renouvelée pour mieux contribuer à mes responsabilités professionnelles.")
				    .dateRequested(LocalDate.now())
				    .status(LeaveStatus.PENDING)
				    .abroad(false) 
				    .country(null)
				    .build();

			Leave leave2 = Leave.builder()
				    .employeeId(2L)
				    .startDate(LocalDate.of(2025, 6, 10))
				    .endDate(LocalDate.of(2025, 6, 12))
				    .daysRequested(3)
				    .type(LeaveType.SICK)
				    .reason("Je dois partir à l'étranger pour des raisons médicales importantes nécessitant un suivi spécialisé.")
				    .dateRequested(LocalDate.now())
				    .status(LeaveStatus.APPROVED)
				    .abroad(true) 
				    .country("France") 
				    .build();
			
			Leave leave3 = Leave.builder()
				    .employeeId(3L)
				    .startDate(LocalDate.of(2025, 7, 1))
				    .endDate(LocalDate.of(2025, 7, 3))
				    .daysRequested(3)
				    .type(LeaveType.VACATION)
				    .reason("Je voulais assister à un événement familial, mais la période n'était pas compatible avec les besoins du service.")
				    .dateRequested(LocalDate.now())
				    .status(LeaveStatus.DENIED)
				    .abroad(false)
				    .country(null)
				    .build();


			leaveRepository.save(leave1);
			leaveRepository.save(leave2);
			leaveRepository.save(leave3);
		};
	}
}
