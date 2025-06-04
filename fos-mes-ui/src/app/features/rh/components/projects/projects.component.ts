import { Component, OnInit } from '@angular/core';
import { EmployeeService } from '../../../../shared/services/employee/employee.service';
import { ClientService } from '../../../../shared/services/client/client.service';
import { EmployeeResponse } from '../../../../shared/models/employee-response.model';
import { Client } from '../../../../shared/models/client.model';
import { Project } from '../../../../shared/models/project.model';
import { ProjectsService } from '../../../../shared/services/projects/projects.service';
import { ProjectStatus } from '../../../../shared/enums/project-status.enum';
import * as FileSaver from 'file-saver';
import ExcelJS from 'exceljs';

declare var bootstrap: any;
@Component({
  selector: 'app-projects',
  standalone: false,
  templateUrl: './projects.component.html',
  styleUrl: './projects.component.css',
})
export class ProjectsComponent implements OnInit {
  isGridView: boolean = true;
  projects: Project[] = [];
  totalProjects: number = 0;
  inProgressProjects: number = 0;
  upcomingProjects: number = 0;
  today: Date = new Date();
  isProjectFormVisible = false;
  isEditMode = false;
  isDownloading: boolean = false;

  constructor(
    private employeeService: EmployeeService,
    private clientService: ClientService,
    private projectsService: ProjectsService
  ) {}

  clients: Client[] = [];
  employees: EmployeeResponse[] = [];
  editingProjectId: number | null = null;
  completedProjects: number = 0;
  currentPage: number = 0;
  pageSize: number = 3;
  totalProjectsCount: number = 0;

  newProject = {
    title: '',
    description: '',
    startDate: null as string | null,
    duration: 1,
    clientId: null as number | null,
    employeeIds: [] as number[],
  };

  ngOnInit(): void {
    this.loadClients();
    this.loadEmployees();
    this.loadProjects();
  }

  switchToListView() {
    this.isGridView = false;
  }

  switchToGridView() {
    this.isGridView = true;
  }

  createProject() {
    this.newProject = {
      title: '',
      description: '',
      startDate: '',
      duration: 1,
      clientId: null,
      employeeIds: [],
    };
    this.isEditMode = false;
    this.isProjectFormVisible = true;
  }

  saveProject() {
    console.log('Projet créé :', this.newProject);

    this.projectsService.createProject(this.newProject).subscribe({
      next: (createdProject) => {
        console.log('Projet sauvegardé avec succès:', createdProject);

        this.projects.push(createdProject);
        this.updateProjectStats();
        this.isProjectFormVisible = false;
        this.loadProjects();

        this.showToast('Projet ajouté avec succès !');
      },
      error: (error) => {
        console.error('Erreur lors de la création du projet', error);
      },
    });
  }

  toggleEmployeeSelection(empId: number) {
    const index = this.newProject.employeeIds.indexOf(empId);
    if (index > -1) {
      this.newProject.employeeIds.splice(index, 1);
    } else {
      this.newProject.employeeIds.push(empId);
    }
  }

  loadClients(page: number = 0, size: number = 10) {
    this.clientService.getClients(page, size).subscribe({
      next: (pageData) => {
        this.clients = pageData.content;
        console.log('Clients chargés :', this.clients);
      },
      error: (error) => {
        console.error('Erreur lors du chargement des clients', error);
      },
    });
  }

  loadEmployees(page: number = 0, size: number = 10) {
    this.employeeService.getAll(page, size).subscribe({
      next: (pageData) => {
        this.employees = pageData.content;
        console.log('Employés chargés :', this.employees);
      },
      error: (err) => {
        console.error('Erreur lors du chargement des employés', err);
      },
    });
  }

  // loadProjects(page: number = 0, size: number = 10) {
  //   this.projectsService.getAllProjects(page, size).subscribe({
  //     next: (pageData) => {
  //       this.projects = pageData.content;
  //       console.log('Projets chargés :', this.projects);
  //       this.updateProjectStats();
  //     },
  //     error: (err) => {
  //       console.error('Erreur lors du chargement des projets', err);
  //     },
  //   });
  // }
  loadProjects(page: number = this.currentPage, size: number = this.pageSize) {
    this.projectsService.getAllProjects(page, size).subscribe({
      next: (pageData) => {
        this.projects = pageData.content;
        this.totalProjectsCount = pageData.totalElements;
        console.log('Projets chargés :', this.projects);
        this.updateProjectStats();
        this.currentPage = page;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des projets', err);
      },
    });
  }

  updateProjectStats() {
    this.totalProjects = this.projects.length;
    this.inProgressProjects = this.projects.filter(
      (p) => p.progress < 100
    ).length;
    this.upcomingProjects = this.projects.filter(
      (p) => p.status === ProjectStatus.NON_COMMENCE
    ).length;
    this.completedProjects = this.projects.filter(
      (p) => p.progress === 100
    ).length;
  }

  editProject(project: Project) {
    this.newProject = {
      title: project.title,
      description: project.description,
      startDate: project.startDate?.toString() ?? null,
      duration: project.duration ?? 1,
      clientId: project.client?.id ?? null,
      employeeIds: project.team?.map((emp) => emp.id) ?? [],
    };
    this.editingProjectId = project.id;
    this.isEditMode = true;
    this.isProjectFormVisible = true;
  }

  updateProject() {
    if (!this.editingProjectId) return;

    const updatedProject = {
      id: this.editingProjectId,
      title: this.newProject.title,
      description: this.newProject.description,
      startDate: this.newProject.startDate,
      duration: this.newProject.duration,
      clientId: this.newProject.clientId,
      employeeIds: this.newProject.employeeIds,
    };

    this.projectsService
      .updateProject(this.editingProjectId, updatedProject)
      .subscribe({
        next: (project) => {
          // Mise à jour locale de la liste projects
          const index = this.projects.findIndex(
            (p) => p.id === this.editingProjectId
          );
          if (index > -1) {
            this.projects[index] = project;
          }
          this.updateProjectStats();
          this.editingProjectId = null;

          this.isProjectFormVisible = false;

          this.showToast('Projet modifié avec succès !');
        },
        error: (error) => {
          console.error('Erreur lors de la mise à jour du projet', error);
        },
      });
  }

  deleteProject(projectId: number) {
    if (!confirm('Voulez-vous vraiment supprimer ce projet ?')) return;

    this.projectsService.deleteProject(projectId).subscribe({
      next: () => {
        this.projects = this.projects.filter((p) => p.id !== projectId);
        this.updateProjectStats();
        console.log('Projet supprimé avec succès');
      },
      error: (error) => {
        console.error('Erreur lors de la suppression du projet', error);
      },
    });
  }

  getClientImageUrl(filename: string): string {
    return `http://localhost:8082/api/clients/image/${filename}`;
  }

  getImageUrl(filename: string): string {
    return `http://localhost:8081/api/employees/image/${filename}`;
  }
  showToast(message: string) {
    const toastEl = document.getElementById('successToast');
    if (toastEl) {
      toastEl.querySelector('.toast-body')!.textContent = message;
      const toast = new bootstrap.Toast(toastEl);
      toast.show();
    }
  }
  goBack() {
    this.isProjectFormVisible = false;
  }

  submitProjectForm(form: any): void {
    if (form.invalid) {
      form.control.markAllAsTouched(); // pour forcer l'affichage des erreurs
      return;
    }

    if (this.isEditMode) {
      this.updateProject();
    } else {
      this.saveProject();
    }
  }
  async downloadExcel() {
    const workbook = new ExcelJS.Workbook();

    // Feuille principale "Projets"
    const projectSheet = workbook.addWorksheet('Projets');

    // Colonnes principales pour les projets
    projectSheet.columns = [
      { header: 'ID Projet', key: 'id', width: 10 },
      { header: 'Titre', key: 'title', width: 30 },
      { header: 'Description', key: 'description', width: 40 },
      { header: 'Date début', key: 'startDate', width: 15 },
      { header: 'Durée (jours)', key: 'duration', width: 15 },
      { header: 'Client', key: 'clientName', width: 25 },
      { header: 'Progression (%)', key: 'progress', width: 15 },
      { header: 'Budget total', key: 'totalBudget', width: 15 },
      { header: 'Équipe', key: 'team', width: 40 },
    ];

    // Fonction utilitaire pour formater la date (jj/mm/aaaa)
    const formatDate = (dateStr: string | null) => {
      if (!dateStr) return '';
      return new Date(dateStr).toLocaleDateString('fr-FR');
    };

    // Ajouter les données des projets
    this.projects.forEach((proj) => {
      // Formater la liste des membres de l'équipe en "Prénom Nom (Poste)"
      const teamStr =
        proj.team
          ?.map(
            (member) =>
              `${member.firstName} ${member.lastName} (${member.position})`
          )
          .join(', ') ?? '';

      projectSheet.addRow({
        id: proj.id,
        title: proj.title,
        description: proj.description || '',
        startDate: formatDate(proj.startDate),
        duration: proj.duration,
        clientName: proj.client
          ? `${proj.client.firstName} ${proj.client.lastName}`
          : '',
        progress: proj.progress,
        totalBudget: proj.totalBudget,
        team: teamStr,
      });
    });

    // Style entête pour "Projets"
    const headerRow = projectSheet.getRow(1);
    headerRow.font = { bold: true, color: { argb: 'FFFFFFFF' } };
    headerRow.fill = {
      type: 'pattern',
      pattern: 'solid',
      fgColor: { argb: 'FF4472C4' }, // bleu foncé
    };

    // --- Feuille Phases ---
    const phasesSheet = workbook.addWorksheet('Phases');

    // Colonnes pour les phases
    phasesSheet.columns = [
      { header: 'ID Phase', key: 'id', width: 10 },
      { header: 'Projet associé', key: 'projectTitle', width: 30 },
      { header: 'Titre Phase', key: 'title', width: 30 },
      { header: 'Description', key: 'description', width: 40 },
      { header: 'Date début', key: 'startDate', width: 15 },
      { header: 'Date fin', key: 'endDate', width: 15 },
      { header: 'Statut', key: 'status', width: 15 },
      { header: 'Type', key: 'type', width: 20 },
      { header: 'Budget', key: 'budget', width: 15 },
    ];

    // Ajouter les données des phases de chaque projet
    this.projects.forEach((proj) => {
      proj.phases?.forEach((phase) => {
        phasesSheet.addRow({
          id: phase.id,
          projectTitle: proj.title,
          title: phase.title,
          description: phase.description || '',
          startDate: formatDate(phase.startdate),
          endDate: formatDate(phase.endDate),
          status: phase.status,
          type: phase.type,
          budget: phase.budget,
        });
      });
    });

    // Style entête pour "Phases"
    const phaseHeaderRow = phasesSheet.getRow(1);
    phaseHeaderRow.font = { bold: true, color: { argb: 'FFFFFFFF' } };
    phaseHeaderRow.fill = {
      type: 'pattern',
      pattern: 'solid',
      fgColor: { argb: 'FF4472C4' },
    };

    // Générer le buffer et déclencher le téléchargement
    const buffer = await workbook.xlsx.writeBuffer();
    const blob = new Blob([buffer], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    });

    FileSaver.saveAs(blob, 'projets_et_phases.xlsx');
  }

  get projectManagers(): EmployeeResponse[] {
    return this.employees.filter((emp) => emp.position === 'PROJECT_MANAGER');
  }

  goToPage(page: number) {
    if (page >= 0 && page < this.totalPages) {
      this.loadProjects(page);
    }
  }

  previousPage() {
    if (this.currentPage > 0) {
      this.loadProjects(this.currentPage - 1);
    }
  }

  nextPage() {
    if (this.currentPage < this.totalPages - 1) {
      this.loadProjects(this.currentPage + 1);
    }
  }

  get totalPages(): number {
    return Math.ceil(this.totalProjectsCount / this.pageSize);
  }

  searchQuery: string = '';

  onSearchChange(): void {
    console.log('on search ....');
  }
}
