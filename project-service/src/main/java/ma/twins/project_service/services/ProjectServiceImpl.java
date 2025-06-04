package ma.twins.project_service.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import ma.twins.project_service.dtos.ProjectRequest;
import ma.twins.project_service.dtos.ProjectResponse;
import ma.twins.project_service.entities.Project;
import ma.twins.project_service.model.Client;
import ma.twins.project_service.model.Employee;
import ma.twins.project_service.repositories.ProjectRepository;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ClientRestService clientRestService;

	@Autowired
	private EmployeeRestService employeeRestService;

	@Override
	public Project createProject(ProjectRequest projectRequest) {
		Project project = Project.builder()
	            .title(projectRequest.getTitle())
	            .description(projectRequest.getDescription())
	            .startDate(projectRequest.getStartDate())
	            .duration(projectRequest.getDuration())
	            .clientId(projectRequest.getClientId())
	            .progress(0)
	            .updatedDate(LocalDate.now())
	            .phases(new ArrayList<>())
	            .teamIds(projectRequest.getEmployeeIds())
	            .build();

	    // 1. Sauvegarde du projet
	    Project savedProject = projectRepository.save(project);
	
	    return savedProject;
	}

	@Override
	public Project updateProject(Long projectId, ProjectRequest projectRequest) {
		Optional<Project> optionalProject = projectRepository.findById(projectId);
		if (optionalProject.isEmpty()) {
			throw new RuntimeException("Project with ID " + projectId + " not found");
		}

		Project project = optionalProject.get();
		project.setTitle(projectRequest.getTitle());
		project.setDescription(projectRequest.getDescription());
		project.setStartDate(projectRequest.getStartDate());
		project.setClientId(projectRequest.getClientId());
		project.setUpdatedDate(LocalDate.now());

		return projectRepository.save(project);
	}

	@Override
	public Project getProjectById(Long projectId) {
		return projectRepository.findById(projectId)
				.orElseThrow(() -> new RuntimeException("Project with ID " + projectId + " not found"));
	}

	@Override
	public void deleteProject(Long projectId) {
		if (!projectRepository.existsById(projectId)) {
			throw new RuntimeException("Project with ID " + projectId + " does not exist");
		}
		projectRepository.deleteById(projectId);

	}


	
	@Override
	public Page<ProjectResponse> getAllProjectResponses(Pageable pageable) {
	    Page<Project> projectsPage = projectRepository.findAll(pageable);

	    return projectsPage.map(project -> {
	        // Récupération du client
	        Client client = null;
	        try {
	            client = clientRestService.clientById(project.getClientId());
	        } catch (Exception e) {
	            System.err.println("Erreur client ID " + project.getClientId() + " : " + e.getMessage());
	        }

	        // Récupération de l'équipe
	        List<Employee> team = new ArrayList<>();
	        if (project.getTeamIds() != null) {
	            for (Long empId : project.getTeamIds()) {
	                try {
	                    Employee employee = employeeRestService.customerById(empId);
	                    team.add(employee);
	                } catch (Exception e) {
	                    System.err.println("Erreur employé ID " + empId + " : " + e.getMessage());
	                }
	            }
	        }

	        return new ProjectResponse(
	            project.getId(),
	            project.getTitle(),
	            project.getDescription(),
	            project.getStartDate(),
	            project.getDuration(),
	            client,
	            project.getProgress(),
	            project.getTotalBudget() ,
	            project.getPhases(),
	            team
	        );
	    });
	}


	@Override
	public Project updateProgress(Long projectId, int progress) {
		Project project = getProjectById(projectId);
		project.setProgress(progress);
		project.setUpdatedDate(LocalDate.now());
		return projectRepository.save(project);
	}
	
	
	public ProjectResponse getProjectResponseById(Long projectId) {
	    Project project = getProjectById(projectId);

	    // Appel au microservice Client
	    Client client =  clientRestService.clientById(project.getClientId());

	    // Ici, pas d'IDs d'employés connus, on retourne une liste vide
	    List<Employee> team = new ArrayList<>();
	    List<Long> teamIds = project.getTeamIds();

	    if (teamIds != null) {
	        for (Long empId : teamIds) {
	            try {
	                Employee employee = employeeRestService.customerById(empId);
	                team.add(employee);
	            } catch (Exception e) {
	                System.err.println("Erreur lors de la récupération de l'employé avec ID " + empId + " : " + e.getMessage());
	            }
	        }
	    }

	    return new ProjectResponse(
	        project.getId(),
	        project.getTitle(),
	        project.getDescription(),
	        project.getStartDate(),
	        project.getDuration(),
	        client,
	        project.getProgress(),
	        project.getTotalBudget() ,
	        project.getPhases(),
	        team
	    );
	}



}
