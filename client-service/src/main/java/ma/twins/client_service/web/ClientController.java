package ma.twins.client_service.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ma.twins.client_service.dtos.ClientRequest;
import ma.twins.client_service.dtos.ClientResponse;
import ma.twins.client_service.services.ClientService;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

	private final ClientService clientService;

	@GetMapping
	@PreAuthorize("hasAuthority('RH')")
	public Page<ClientResponse> getAllClients(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		return clientService.getAllClients(pageable);
	}

	@PostMapping
	@PreAuthorize("hasAuthority('RH')")
	public ClientResponse saveClient(@RequestBody ClientRequest client) {
		return clientService.saveClient(client);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('RH')")
	public ResponseEntity<ClientResponse> getClientById(@PathVariable Long id) {
		return clientService.getClientById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('RH')")
	public ClientResponse updateClient(@PathVariable Long id, @RequestBody ClientRequest clientRequest) {
	    return clientService.updateClient(id, clientRequest);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('RH')")
	public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
		clientService.deleteClient(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/search")
	@PreAuthorize("hasAuthority('RH')")
	public ResponseEntity<?> searchClients(@RequestParam String keyword) {
	    return ResponseEntity.ok(clientService.searchClients(keyword));
	}
	
	@GetMapping("/image/{filename:.+}")
	@PreAuthorize("hasAuthority('RH')")
	public ResponseEntity<byte[]> getEmployeeImage(@PathVariable String filename) {
	    try {
	        Path imagePath = Paths.get("src","uploads", "clients", filename).toAbsolutePath().normalize();
	        System.out.println("Loading file from: " + imagePath);

	        if (!Files.exists(imagePath)) {
	            System.err.println("File not found: " + imagePath);
	            return ResponseEntity.notFound().build();
	        }

	        byte[] imageBytes = Files.readAllBytes(imagePath);

	        String contentType = Files.probeContentType(imagePath);
	        if (contentType == null) {
	            contentType = "application/octet-stream";
	        }

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.parseMediaType(contentType));
	        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
	    } catch (NoSuchFileException e) {
	        System.err.println("File not found: " + e.getFile());
	        return ResponseEntity.notFound().build();
	    } catch (IOException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

}
