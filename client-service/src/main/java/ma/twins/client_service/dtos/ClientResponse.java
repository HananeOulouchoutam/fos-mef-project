package ma.twins.client_service.dtos;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;
import ma.twins.client_service.enums.ClientStatus;


@Data
@Builder
public class ClientResponse {
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String imageUrl ;
	private Long projectId;
	private String report;
	private LocalDate createdAt;
	private ClientStatus status;
}
