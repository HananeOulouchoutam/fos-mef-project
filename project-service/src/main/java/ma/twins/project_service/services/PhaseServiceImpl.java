package ma.twins.project_service.services;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ma.twins.project_service.dtos.PhaseRequest;
import ma.twins.project_service.entities.Phase;
import ma.twins.project_service.entities.Project;
import ma.twins.project_service.enums.Status;
import ma.twins.project_service.repositories.PhaseRepository;
import ma.twins.project_service.repositories.ProjectRepository;



@Transactional
@Service
public class PhaseServiceImpl implements PhaseService{
	
	
	private final PhaseRepository phaseRepository;
    private final ProjectRepository projectRepository;

    public PhaseServiceImpl(PhaseRepository phaseRepository, ProjectRepository projectRepository) {
        this.phaseRepository = phaseRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public Phase addPhase(PhaseRequest phaseRequest) {
    	 // Récupérer le projet associé
        Project project = projectRepository.findById(phaseRequest.getProjectId())
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));

        // Créer la phase
        Phase phase = new Phase();
        phase.setTitle(phaseRequest.getTitle());
        phase.setDescription(phaseRequest.getDescription());
        phase.setStartdate(phaseRequest.getStartDate());
        phase.setEndDate(phaseRequest.getEndDate());
        phase.setStatus(Status.NOT_STARTED); 
        phase.setType(phaseRequest.getType());
        phase.setBudget(phaseRequest.getBudget());
        phase.setProject(project);

        // Sauvegarder la phase
        Phase savedPhase = phaseRepository.save(phase);

        // ⚠️ Recalculer le progrès du projet
        project.setProgress(project.calculateProgress());
        project.setTotalBudget(project.calculateTotalBudget());
        projectRepository.save(project);

        return savedPhase;
    }

    @Override
    public void deletePhase(Long phaseId) {
    	Phase phase = phaseRepository.findById(phaseId)
    	        .orElseThrow(() -> new RuntimeException("Phase non trouvée"));

    	    Project project = phase.getProject();

    	    phaseRepository.deleteById(phaseId);

    	    if (project != null) {
    	        project.setProgress(project.calculateProgress());
    	        project.setTotalBudget(project.calculateTotalBudget());
    	        projectRepository.save(project);
    	    }
    }

    @Override
    public Phase updatePhase(Long phaseId, PhaseRequest phaseRequest) {
        Phase phase = phaseRepository.findById(phaseId)
            .orElseThrow(() -> new RuntimeException("Phase non trouvée"));

        // Mettre à jour les champs de la phase
        phase.setTitle(phaseRequest.getTitle());
        phase.setDescription(phaseRequest.getDescription());
        phase.setStartdate(phaseRequest.getStartDate());
        phase.setEndDate(phaseRequest.getEndDate());
        phase.setType(phaseRequest.getType());
        phase.setUpdatedAt(LocalDate.now());
        phase.setBudget(phaseRequest.getBudget());

        if (phaseRequest.getStatus() != null) {
            phase.setStatus(phaseRequest.getStatus());
        }

        Phase updatedPhase = phaseRepository.save(phase);

        // ⚠️ Récupérer le projet et recalculer le progrès
        Project project = phase.getProject(); // Assure-toi que Phase a bien un champ `project`
        if (project != null) {
            // Important : recharger les phases si elles ne sont pas lazy loaded
            project.setProgress(project.calculateProgress());
            project.setTotalBudget(project.calculateTotalBudget());
            projectRepository.save(project);
        }

        return updatedPhase;
    }

}
