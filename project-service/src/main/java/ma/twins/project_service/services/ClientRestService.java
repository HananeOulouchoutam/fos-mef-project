package ma.twins.project_service.services;

import org.springframework.cloud.openfeign.FeignClient;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ma.twins.project_service.model.Client;



@FeignClient(name="CLIENT-SERVICE")
public interface ClientRestService {
	@GetMapping("/clients/{id}?projection=clientProjection")
	public Client clientById(@PathVariable Long id );
		
}
