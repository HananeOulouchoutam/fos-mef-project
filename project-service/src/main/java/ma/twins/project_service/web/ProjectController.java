package ma.twins.project_service.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ma.twins.project_service.dtos.ProjectRequest;
import ma.twins.project_service.dtos.ProjectResponse;
import ma.twins.project_service.entities.Project;
import ma.twins.project_service.services.ProjectService;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/projects")
@Validated
public class ProjectController {

	@Autowired
    private ProjectService projectService;
	
	@PostMapping
	@PreAuthorize("hasAuthority('RH')")
    public ResponseEntity<Project> createProject(@RequestBody ProjectRequest request) {
        Project project = projectService.createProject(request);
        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('RH')")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody ProjectRequest request) {
        Project updatedProject = projectService.updateProject(id, request);
        return ResponseEntity.ok(updatedProject);
    }
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('RH')")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        ProjectResponse project = projectService.getProjectResponseById(id);
        return ResponseEntity.ok(project);
    }
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('RH')")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
	
	@GetMapping
	@PreAuthorize("hasAuthority('RH')")
    public ResponseEntity<Page<ProjectResponse>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProjectResponse> projects = projectService.getAllProjectResponses(pageable);
        return ResponseEntity.ok(projects);
    }
	
	
	@PatchMapping("/{id}/progress")
	@PreAuthorize("hasAuthority('RH')")
	public ResponseEntity<Project> updateProjectProgress(
	            @PathVariable Long id,
	            @RequestParam int progress
	    ) {
	        Project project = projectService.updateProgress(id, progress);
	        return ResponseEntity.ok(project);
	    }
	
	
}
