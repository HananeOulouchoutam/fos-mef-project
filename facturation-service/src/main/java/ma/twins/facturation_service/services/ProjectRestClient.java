package ma.twins.facturation_service.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ma.twins.facturation_service.model.Project;


@FeignClient(name = "PROJECT-SERVICE")
public interface ProjectRestClient {

    @GetMapping("/projects/{id}?projection=projectProjection")
    public Project getProjetById(@PathVariable("id") Long id);
}