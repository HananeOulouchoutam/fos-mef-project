package ma.twins.facturation_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.twins.facturation_service.entities.Facturation;

public interface FacturationRepository extends JpaRepository<Facturation,Long> {

}
