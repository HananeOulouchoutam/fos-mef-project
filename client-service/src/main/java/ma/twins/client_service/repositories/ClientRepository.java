package ma.twins.client_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ma.twins.client_service.entities.Client;

public interface ClientRepository extends JpaRepository<Client,Long> {
	@Query("SELECT c FROM Client c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Client> searchByFirstNameOrLastName(@Param("keyword") String keyword);
}
