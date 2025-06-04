package ma.twins.project_service.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import ma.twins.project_service.dtos.PhaseRequest;
import ma.twins.project_service.entities.Phase;

import ma.twins.project_service.services.PhaseService;

@RestController
@RequestMapping("/api/phases")
public class PhaseController {

    private final PhaseService phaseService;

    public PhaseController(PhaseService phaseService) {
        this.phaseService = phaseService;
    }

    // Ajouter une phase
    @PostMapping
    public ResponseEntity<Phase> addPhase(@RequestBody PhaseRequest phaseRequest) {
        Phase createdPhase = phaseService.addPhase(phaseRequest);
        return new ResponseEntity<>(createdPhase, HttpStatus.CREATED);
    }

    // Supprimer une phase par ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhase(@PathVariable("id") Long id) {
        phaseService.deletePhase(id);
        return ResponseEntity.noContent().build();
    }

    // Mettre à jour le statut d'une phase
    @PutMapping("/{id}/edit")
    public ResponseEntity<Phase> updateStatus(
            @PathVariable("id") Long id,
            @RequestBody PhaseRequest phaseRequest) {
        Phase updatedPhase = phaseService.updatePhase(id, phaseRequest);
        return ResponseEntity.ok(updatedPhase);
    }
}
