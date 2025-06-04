package ma.twins.project_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.twins.project_service.entities.Project;



public interface ProjectRepository extends JpaRepository<Project, Long>{

}
