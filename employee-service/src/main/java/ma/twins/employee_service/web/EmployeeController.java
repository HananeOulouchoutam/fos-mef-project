package ma.twins.employee_service.web;


import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import dtos.EmployeeRequest;
import dtos.EmployeeResponse;
import ma.twins.employee_service.enums.Position;
import ma.twins.employee_service.services.EmployeeServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;





@RestController
@RequestMapping("api/employees")

public class EmployeeController {
    
	@Autowired
	private EmployeeServiceImpl empService ;
	
	
	@PostMapping("")
	@PreAuthorize("hasAuthority('RH')")
	EmployeeResponse addNewEmployee(@RequestBody EmployeeRequest em) {
		return empService.addEmployee(em);
	}
	
	@GetMapping("")
	@PreAuthorize("hasAuthority('RH')")
	public ResponseEntity<Page<EmployeeResponse>> getEmployees(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "5") int size
	) {
	    Pageable pageable = PageRequest.of(page, size);
	    return ResponseEntity.ok(empService.getEmployees(pageable));
	}
	
	@GetMapping("/search")
	@PreAuthorize("hasAuthority('RH')")
	public ResponseEntity<List<EmployeeResponse>> searchEmployees(
	        @RequestParam(required = false) Position position,
	        @RequestParam(required = false) String keyword
	) {
	    List<EmployeeResponse> results = empService.searchEmployees(position, keyword != null ? keyword : "");
	    return ResponseEntity.ok(results);
	}
	
	@GetMapping("/image/{filename:.+}")
	@PreAuthorize("hasAuthority('RH')")
	public ResponseEntity<byte[]> getEmployeeImage(@PathVariable String filename) {
	    try {
	        Path imagePath = Paths.get("src","resources" ,"uploads", "profiles", filename).toAbsolutePath().normalize();
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
	
	@GetMapping("/count")
	@PreAuthorize("hasAuthority('RH')")
	public ResponseEntity<Long> countEmployees() {
	    long count = empService.countEmployees();
	    return ResponseEntity.ok(count);
	}
	
	@GetMapping("/cnss/{filename:.+}")
	@PreAuthorize("hasAuthority('RH')")
	public ResponseEntity<byte[]> getEmployeeCnss(@PathVariable String filename) {
	    return serveFile("cnss", filename);
	}

	@GetMapping("/cin/{filename:.+}")
	@PreAuthorize("hasAuthority('RH')")
	public ResponseEntity<byte[]> getEmployeeCin(@PathVariable String filename) {
	    return serveFile("cin", filename);
	}
	
	private ResponseEntity<byte[]> serveFile(String folder, String filename) {
	    try {
	        Path filePath = Paths.get("src", "uploads", folder, filename).toAbsolutePath().normalize();
	        if (!Files.exists(filePath)) {
	            return ResponseEntity.notFound().build();
	        }

	        byte[] fileBytes = Files.readAllBytes(filePath);

	        String contentType = Files.probeContentType(filePath);
	        if (contentType == null) {
	            contentType = "application/octet-stream";
	        }

	        if (filename.toLowerCase().endsWith(".pdf")) {
	            contentType = "application/pdf";
	        }

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.parseMediaType(contentType));
	        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"");

	        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
	    } catch (IOException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

}



