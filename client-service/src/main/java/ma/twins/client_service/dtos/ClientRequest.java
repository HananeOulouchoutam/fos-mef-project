package ma.twins.client_service.dtos;

import lombok.Data;

@Data
public class ClientRequest {
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private Long projectId;
	private String report;
}
