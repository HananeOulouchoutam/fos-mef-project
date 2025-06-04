package ma.twins.facturation_service.services;

import java.util.List;

import ma.twins.facturation_service.entities.Facturation;
import ma.twins.facturation_service.model.FacturationRequest;

public interface FacturationService {

    Facturation createFacturation(FacturationRequest request);
    List<Facturation> getAllFacturations();
    Facturation getFacturationById(Long id);
    void deleteFacturation(Long id);

}
