package ma.twins.client_service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ma.twins.client_service.entities.Client;
import ma.twins.client_service.enums.ClientStatus;
import ma.twins.client_service.repositories.ClientRepository;

@SpringBootApplication
public class ClientServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientServiceApplication.class, args);
	}
	
	@Bean
	CommandLineRunner initDatabase(ClientRepository clientRepository) {
	    return args -> {
	        clientRepository.save(Client.builder()
	                .firstName("Loubna")
	                .lastName("Oulouchoutam")
	                .email("oulouchoutamloubna@gmail.com")
	                .phoneNumber("0600123456")
	                .status(ClientStatus.ACTIVE)
	                .projectId(1L)
	                .report("Client intéressé par notre solution IA.")
	                .imageUrl("pr_loubnaoulouchoutam.jpg")
	                .build());

	        clientRepository.save(Client.builder()
	                .firstName("Mohamed")
	                .lastName("Oulouchoutam")
	                .email("oulouchoutammohamed@gmail.com")
	                .phoneNumber("0600789123")
	                .status(ClientStatus.ACTIVE)
	                .projectId(2L)
	                .report("Client en phase de test produit.")
	                .imageUrl("pr_mohamedoulouchoutam.jpg")
	                .build());

//	        clientRepository.save(Client.builder()
//	                .firstName("Nora")
//	                .lastName("Bennani")
//	                .email("nora.bennani@example.com")
//	                .phoneNumber("0600345678")
//	                .status(ClientStatus.INACTIVE)
//	                .projectId(3L)
//	                .report("Client fidèle, feedback positif.")
//	                .imageUrl("pro.jpg")
//	                .build());
	    };
	}


}
