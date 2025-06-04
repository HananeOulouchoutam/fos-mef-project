package ma.twins.client_service.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.twins.client_service.dtos.ClientRequest;
import ma.twins.client_service.dtos.ClientResponse;
import ma.twins.client_service.entities.Client;
import ma.twins.client_service.enums.ClientStatus;
import ma.twins.client_service.mappers.ClientMapper;
import ma.twins.client_service.repositories.ClientRepository;

@Service
@RequiredArgsConstructor

public class ClientServiceImpl implements ClientService {

	private final ClientRepository clientRepository;

	@Override
	public Page<ClientResponse> getAllClients(Pageable pageable) {
		Page<Client> clients = clientRepository.findAll(pageable);
		return clients.map(ClientMapper::convertToClientResponse);
	}

	@Override
	public ClientResponse saveClient(ClientRequest clientRequest) {
		Client client = new Client();
		client.setFirstName(clientRequest.getFirstName());
		client.setLastName(clientRequest.getLastName());
		client.setEmail(clientRequest.getEmail());
		client.setPhoneNumber(clientRequest.getPhoneNumber());
		client.setProjectId(clientRequest.getProjectId());
		client.setReport(clientRequest.getReport());
		client.setCreatedAt(LocalDate.now());
		client.setUpdatedAt(LocalDate.now());
		client.setStatus(ClientStatus.ACTIVE); 

		Client savedClient = clientRepository.save(client);
		return ClientMapper.convertToClientResponse(savedClient);
	}

	@Override
	public Optional<ClientResponse> getClientById(Long id) {
		Optional<Client> client = clientRepository.findById(id);
		return client.map(ClientMapper::convertToClientResponse);
	}

	@Override
	public void deleteClient(Long id) {
		clientRepository.deleteById(id);
	}

	@Override
	public List<ClientResponse> searchClients(String keyword) {
		List<Client> clients = clientRepository.searchByFirstNameOrLastName(keyword);
		return clients.stream().map(ClientMapper::convertToClientResponse).collect(Collectors.toList());
	}
   
	
	@Override
	public ClientResponse updateClient(Long id, ClientRequest clientRequest) {
		Optional<Client> existingClientOptional = clientRepository.findById(id);
		if (existingClientOptional.isPresent()) {
			Client existingClient = existingClientOptional.get();
			existingClient.setFirstName(clientRequest.getFirstName());
			existingClient.setLastName(clientRequest.getLastName());
			existingClient.setEmail(clientRequest.getEmail());
			existingClient.setPhoneNumber(clientRequest.getPhoneNumber());
			existingClient.setProjectId(clientRequest.getProjectId());
			existingClient.setReport(clientRequest.getReport());
			existingClient.setUpdatedAt(LocalDate.now());

			Client updatedClient = clientRepository.save(existingClient);
			return ClientMapper.convertToClientResponse(updatedClient);
		}
		throw new RuntimeException("Client not found with ID: " + id);
	}

	
}
