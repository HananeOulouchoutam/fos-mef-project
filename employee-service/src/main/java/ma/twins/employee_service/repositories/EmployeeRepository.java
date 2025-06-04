package ma.twins.employee_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ma.twins.employee_service.entities.Employee;
import ma.twins.employee_service.enums.Position;



@RepositoryRestResource
public interface EmployeeRepository extends JpaRepository<Employee, Long>{
	@Query("SELECT e FROM Employee e WHERE e.position = :position OR e.firstName LIKE %:firstNameKeyword% OR e.lastName LIKE %:lastNameKeyword%")
	List<Employee> findByPositionOrFirstNameLikeOrLastNameLike(
	        @Param("position") Position position,
	        @Param("firstNameKeyword") String firstNameKeyword,
	        @Param("lastNameKeyword") String lastNameKeyword
	);



}
