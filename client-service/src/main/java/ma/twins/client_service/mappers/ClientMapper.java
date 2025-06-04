package ma.twins.client_service.mappers;

import ma.twins.client_service.dtos.ClientResponse;
import ma.twins.client_service.entities.Client;

public class ClientMapper {
	
	public static ClientResponse convertToClientResponse(Client client) {
        return ClientResponse.builder()
                .id(client.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .email(client.getEmail())
                .phoneNumber(client.getPhoneNumber())
                .projectId(client.getProjectId())
                .report(client.getReport())
                .createdAt(client.getCreatedAt())
                .status(client.getStatus())
                .imageUrl(client.getImageUrl())
                .build();
    }

}
