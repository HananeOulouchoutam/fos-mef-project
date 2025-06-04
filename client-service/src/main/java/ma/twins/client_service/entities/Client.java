package ma.twins.client_service.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.twins.client_service.enums.ClientStatus;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String imageUrl ;
	private Long projectId;
	private String report;
	private LocalDate createdAt ;
	private LocalDate updatedAt ;
	private ClientStatus status ;
}
