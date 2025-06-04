package ma.twins.facturation_service.web;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ma.twins.facturation_service.entities.Facturation;
import ma.twins.facturation_service.model.FacturationRequest;
import ma.twins.facturation_service.services.FacturationService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


@RestController
@RequestMapping("/api/facturations")
public class FacturationController {
	
	@Autowired
    private FacturationService facturationService;
	

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Facturation createFacturation(@RequestBody FacturationRequest facturation) {
        return facturationService.createFacturation(facturation);
    }

    @GetMapping
    public List<Facturation> getAllFacturations() {
        return facturationService.getAllFacturations();
    }


    @GetMapping("/{id}")
    public Facturation getFacturationById(@PathVariable Long id) {
        return facturationService.getFacturationById(id);
    }

    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFacturation(@PathVariable Long id) {
        facturationService.deleteFacturation(id);
    }
    
    @GetMapping("/pdf/{filename:.+}")
    public ResponseEntity<Resource> getPdf(@PathVariable String filename) {
        Path file = Paths.get("src/main/resources/facturations").resolve(filename);
        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename)
            .body(resource);
    }

}
