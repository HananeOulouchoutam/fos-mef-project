package ma.twins.project_service.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ma.twins.project_service.dtos.ProjectRequest;
import ma.twins.project_service.dtos.ProjectResponse;
import ma.twins.project_service.entities.Project;

public interface ProjectService {
  Project createProject(ProjectRequest projectRequest);
  Project updateProject(Long projectId, ProjectRequest projectRequest);
  Project getProjectById(Long projectId);
  void deleteProject(Long projectId);
//  Page<Project> getAllProjects(Pageable pageable);
  Page<ProjectResponse> getAllProjectResponses(Pageable pageable);
  Project updateProgress(Long projectId, int progress);
  ProjectResponse getProjectResponseById(Long projectId);
  
}
