package ma.twins.client_service.services;


import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ma.twins.client_service.dtos.ClientRequest;
import ma.twins.client_service.dtos.ClientResponse;


public interface ClientService {
	
	Page<ClientResponse> getAllClients(Pageable pageable);
	ClientResponse saveClient(ClientRequest clientRequest);
	Optional<ClientResponse> getClientById(Long id);
	void deleteClient(Long id);
	List<ClientResponse> searchClients(String keyword);
	ClientResponse updateClient(Long id, ClientRequest clientRequest); 

}
