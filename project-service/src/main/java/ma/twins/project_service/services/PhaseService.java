package ma.twins.project_service.services;

import ma.twins.project_service.dtos.PhaseRequest;
import ma.twins.project_service.entities.Phase;


public interface PhaseService {
	
	   Phase addPhase(PhaseRequest phaseRequest);
       void deletePhase(Long phaseId);
       Phase updatePhase(Long phaseId, PhaseRequest phaseRequest);

}
