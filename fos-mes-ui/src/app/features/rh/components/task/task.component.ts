import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Project } from '../../../../shared/models/project.model';
import { ProjectsService } from '../../../../shared/services/projects/projects.service';
import { PhasesService } from '../../../../shared/services/phases/phases.service';
import { PhaseRequest } from '../../../../shared/models/phase-request.model';
import { TypePhase } from '../../../../shared/enums/phase-type.enum';
import { Phase } from '../../../../shared/models/phase.model';
import { BillingsService } from '../../../../shared/services/billings/billings.service';
import { FacturationRequest } from '../../../../shared/models/facturation-request.model';
import { NgForm } from '@angular/forms';

declare var bootstrap: any;
@Component({
  selector: 'app-task',
  standalone: false,
  templateUrl: './task.component.html',
  styleUrl: './task.component.css',
})
export class TaskComponent implements OnInit {
  projectId!: number;
  project!: Project;
  errorMessage?: string;
  showPhaseForm = false;

  newPhase = {
    title: '',
    description: '',
    startDate: '',
    endDate: '',
    type: null as TypePhase | null,
    budget: 0,
    status: 'NOT_STARTED' as
      | 'NOT_STARTED'
      | 'IN_PROGRESS'
      | 'REVIEW'
      | 'COMPLETED',
  };

  TypePhase = TypePhase;
  phaseTypes = Object.values(TypePhase);
  isEditMode = false;
  editingPhaseId?: number;
  showHover = false;

  constructor(
    private route: ActivatedRoute,
    private projectsService: ProjectsService,
    private phasesService: PhasesService,
    private billingsService: BillingsService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.projectId = +params['id'];
      this.loadProject();
    });
  }

  loadProject(): void {
    this.projectsService.getProjectById(this.projectId).subscribe({
      next: (project) => {
        console.log('Résultat du backend :', project);
        this.project = project;
      },
      error: (err) => {
        this.errorMessage = 'Erreur lors du chargement du projet.';
        console.error(err);
      },
    });
  }

  onSubmitPhase(form: NgForm) {
    if (form.invalid) {
      Object.values(form.controls).forEach((control) => {
        control.markAsTouched();
      });
      return;
    }

    const phaseRequest: PhaseRequest = {
      title: this.newPhase.title,
      description: this.newPhase.description,
      startDate: this.newPhase.startDate,
      endDate: this.newPhase.endDate,
      projectId: this.projectId,
      type: this.newPhase.type!,
      status: this.newPhase.status,
      budget: this.newPhase.budget,
    };

    const isCompleted = this.newPhase.status === 'COMPLETED';

    const createFacturationIfNeeded = (phase: Phase) => {
      if (isCompleted) {
        const facturation: FacturationRequest = {
          clientId: this.project.client.id,
          clientName:
            this.project.client.firstName + this.project.client.lastName,
          clientEmail: this.project.client.email,
          clientPhoneNumber: this.project.client.phoneNumber,
          projectId: this.project.id,
          projectTitle: this.project.title,
          phaseId: phase.id,
          phaseTitle: phase.title,
          startDate: phase.startdate,
          endDate: phase.endDate,
          amountHT: phase.budget,
          tva: 0.2 * phase.budget, // exemple TVA à 20%
        };

        this.billingsService.createFacturation(facturation).subscribe({
          next: () => console.log('Facture créée avec succès'),
          error: (err) =>
            console.error('Erreur lors de la création de la facture:', err),
        });
      }
    };

    if (this.isEditMode && this.editingPhaseId) {
      this.phasesService
        .updatePhase(this.editingPhaseId, phaseRequest)
        .subscribe({
          next: (phase) => {
            createFacturationIfNeeded(phase);
            this.afterSavePhase();
            this.showSuccessToast('Phase modifiée avec succès !');
          },
          error: (err) => {
            console.error('Erreur lors de la mise à jour de la phase:', err);
          },
        });
    } else {
      this.phasesService.addPhase(phaseRequest).subscribe({
        next: (phase) => {
          createFacturationIfNeeded(phase);
          this.afterSavePhase();
          this.showSuccessToast('Phase ajoutée avec succès !');
        },
        error: (err) => {
          console.error("Erreur lors de l'ajout de la phase:", err);
        },
      });
    }
  }

  afterSavePhase() {
    this.showPhaseForm = false;
    this.newPhase = {
      title: '',
      description: '',
      startDate: '',
      endDate: '',
      type: null,
      budget: 0,
      status: 'NOT_STARTED',
    };
    this.isEditMode = false;
    this.editingPhaseId = undefined;
    this.loadProject();
  }

  onEditPhase(phase: Phase) {
    this.isEditMode = true;
    this.editingPhaseId = phase.id;
    this.newPhase = {
      title: phase.title,
      description: phase.description,
      startDate: phase.startdate,
      endDate: phase.endDate,
      type: phase.type,
      budget: phase.budget,
      status: phase.status,
    };
    this.showPhaseForm = true;
  }

  onDeletePhase(phase: Phase) {
    if (confirm(`Voulez-vous vraiment supprimer la phase "${phase.title}" ?`)) {
      this.phasesService.deletePhase(phase.id).subscribe({
        next: () => {
          this.loadProject();
        },
        error: (err) => {
          console.error('Erreur lors de la suppression de la phase:', err);
        },
      });
    }
  }

  getClientImageUrl(filename: string): string {
    return `http://localhost:8082/api/clients/image/${filename}`;
  }

  getImageUrl(filename: string): string {
    return `http://localhost:8081/api/employees/image/${filename}`;
  }
  get teamPreview() {
    return this.project?.team ? this.project.team.slice(0, 2) : [];
  }

  openInvoicePdf(phase: Phase): void {
    const pdfPath = this.billingsService.getPdfUrl(
      `facture_${this.projectId}_${phase.id}.pdf`
    );
    window.open(pdfPath, '_blank');
  }
  openCreatePhaseForm() {
    this.isEditMode = false;
    this.editingPhaseId = undefined;
    this.newPhase = {
      title: '',
      description: '',
      startDate: '',
      endDate: '',
      type: null,
      budget: 0,
      status: 'NOT_STARTED',
    };
    this.showPhaseForm = true;
  }

  cancelPhase() {
    this.showPhaseForm = false;
    this.isEditMode = false;
    this.editingPhaseId = undefined;
  }
  goBack() {
    this.showPhaseForm = false;
  }

  showSuccessToast(message: string) {
    // Modifier le texte du toast
    const toastBody = document.querySelector('#successToast .toast-body');
    if (toastBody) {
      toastBody.textContent = message;
    }

    // Initialiser et afficher le toast Bootstrap
    const toastEl = document.getElementById('successToast');
    if (toastEl) {
      const toast = new bootstrap.Toast(toastEl); // Assure-toi que bootstrap JS est importé dans ton projet
      toast.show();
    }
  }
}
