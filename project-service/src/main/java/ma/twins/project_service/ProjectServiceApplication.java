package ma.twins.project_service;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ProjectServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectServiceApplication.class, args);
	}

	
//	 @Bean
//	 CommandLineRunner initData(ProjectRepository projectRepository) {
//	        return args -> {
//
//	            Project project1 = Project.builder()
//	                    .title("Application Mobile RH")
//	                    .description("Application mobile pour la gestion des employés")
//	                    .startDate(LocalDate.of(2024, 9, 1))
//	                    .clientId(1L)
//	                    .progress(20)
//	                    .updatedDate(LocalDate.now())
//	                    .build();
//
//	            Phase phase1 = Phase.builder()
//	                    .title("Conception")
//	                    .description("Conception de l’architecture de l’application")
//	                    .startdate(LocalDate.of(2024, 9, 1))
//	                    .endDate(LocalDate.of(2024, 9, 10))
//	                    .updatedAt(LocalDate.now())
//	                    .status(Status.IN_PROGRESS)
//	                    .project(project1)
//	                    .build();
//
//	            Phase phase2 = Phase.builder()
//	                    .title("Développement Frontend")
//	                    .description("Développement de l’interface utilisateur mobile")
//	                    .startdate(LocalDate.of(2024, 9, 11))
//	                    .endDate(LocalDate.of(2024, 10, 5))
//	                    .updatedAt(LocalDate.now())
//	                    .status(Status.NOT_STARTED)
//	                    .project(project1)
//	                    .build();
//
//	            project1.setPhases(Arrays.asList(phase1, phase2));
//	            projectRepository.save(project1);
//
//	            // Projet 2
//	            Project project2 = Project.builder()
//	                    .title("Plateforme Web E-learning")
//	                    .description("Développement d’une plateforme pour cours en ligne")
//	                    .startDate(LocalDate.of(2024, 8, 15))
//	                    .clientId(2L)
//	                    .progress(45)
//	                    .updatedDate(LocalDate.now())
//	                    .build();
//
//	            Phase phase3 = Phase.builder()
//	                    .title("Étude de besoins")
//	                    .description("Collecte et analyse des besoins des utilisateurs")
//	                    .startdate(LocalDate.of(2024, 8, 15))
//	                    .endDate(LocalDate.of(2024, 8, 30))
//	                    .updatedAt(LocalDate.now())
//	                    .status(Status.COMPLETED)
//	                    .project(project2)
//	                    .build();
//
//	            Phase phase4 = Phase.builder()
//	                    .title("Backend API")
//	                    .description("Mise en place des microservices")
//	                    .startdate(LocalDate.of(2024, 9, 1))
//	                    .endDate(LocalDate.of(2024, 9, 20))
//	                    .updatedAt(LocalDate.now())
//	                    .status(Status.IN_PROGRESS)
//	                    .project(project2)
//	                    .build();
//
//	            project2.setPhases(Arrays.asList(phase3, phase4));
//	            projectRepository.save(project2);
//
//	            System.out.println("✅ Deux projets ont été initialisés avec succès !");
//	        };
//	    }
}
