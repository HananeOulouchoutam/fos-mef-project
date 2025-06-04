package ma.twins.employee_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ma.twins.employee_service.entities.Competency;


@RepositoryRestResource
public interface CompetencyRepository extends JpaRepository<Competency, Long>{

}
