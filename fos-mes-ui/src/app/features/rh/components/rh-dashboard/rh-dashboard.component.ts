import { Component, OnInit } from '@angular/core';
import { Project } from '../../../../shared/models/project.model';
import { ProjectsService } from '../../../../shared/services/projects/projects.service';
import { EmployeeService } from '../../../../shared/services/employee/employee.service';

@Component({
  selector: 'app-rh-dashboard',
  standalone: false,
  templateUrl: './rh-dashboard.component.html',
  styleUrl: './rh-dashboard.component.css',
})
export class RhDashboardComponent implements OnInit {
  totalRevenue: number = 0;
  projectscount: string = '';
  resources: string = '';
  paidProjectsCount: number = 0;
  unpaidProjectsCount: number = 0;
  projects: Project[] = [];

  lastYearRevenue: number = 25997 ;
  lastYearResources: number = 2;
  lastYearProjectsCount: number = 3;
  paidBudget = 0;
  unpaidBudget = 0;

  percentageChangeRevenue: string = '';
  percentageChangeProjects: string = '';
  percentageChangeResources: string = '';

  series: number[] = [0, 0, 0];
  labels: string[] = ['Terminés', 'En cours', 'Non commencés'];

  monthlySalesData: number[] = [30, 40, 35, 50, 49, 60, 70, 91, 125];
  months: string[] = [
    'Jan',
    'Feb',
    'Mar',
    'Apr',
    'May',
    'Jun',
    'Jul',
    'Aug',
    'Sep',
  ];

  constructor(
    private projectsService: ProjectsService,
    private employeeService: EmployeeService
  ) {}

  ngOnInit(): void {
    this.fetchProjects();
    this.fetchEmployeeCount();
  }

  fetchProjects(): void {
    this.projectsService.getAllProjects().subscribe({
      next: (response) => {
        // ✅ Trier les projets par date de début descendante et prendre les 5 premiers
        this.projects = response.content
          .sort(
            (a: any, b: any) =>
              new Date(b.startDate).getTime() - new Date(a.startDate).getTime()
          )
          .slice(0, 5);

        const totalProjects = this.projects.length;

        const completedCount = this.projects.filter(
          (p) => p.progress === 100
        ).length;

        const inProgressCount = this.projects.filter(
          (p) => p.progress > 0 && p.progress < 100
        ).length;

        const notStartedCount = this.projects.filter(
          (p) => p.progress === 0
        ).length;

        if (totalProjects > 0) {
          this.series = [
            Math.round((completedCount / totalProjects) * 100),
            Math.round((inProgressCount / totalProjects) * 100),
            Math.round((notStartedCount / totalProjects) * 100),
          ];
        } else {
          this.series = [0, 0, 0];
        }

        this.totalRevenue = this.projects.reduce((total, project) => {
          return total + (project.totalBudget || 0);
        }, 0);

        this.paidBudget = this.projects
          .map((project) =>
            project.phases
              .filter((phase) => phase.status === 'COMPLETED')
              .reduce((sum, phase) => sum + phase.budget, 0)
          )
          .reduce((total, projectPaidBudget) => total + projectPaidBudget, 0);

        this.unpaidBudget = this.totalRevenue - this.paidBudget;

        this.projectscount = `${totalProjects}`;
        this.paidProjectsCount = completedCount;
        this.unpaidProjectsCount = totalProjects - completedCount;

        this.calculatePercentageChanges();
      },
      error: (error) => {
        console.error('Erreur lors du chargement des projets :', error);
      },
    });
  }

  fetchEmployeeCount(): void {
    this.employeeService.getEmployeeCount().subscribe({
      next: (count) => {
        this.resources = `${count}`;

        // Calcul des pourcentages de changement
        this.calculatePercentageChanges();
      },
      error: (error) => {
        console.error(
          'Erreur lors du chargement du nombre d’employés :',
          error
        );
      },
    });
  }

  calculatePercentageChanges(): void {
    // On convertit projectscount et resources en nombres
    const currentProjects = Number(this.projectscount) || 0;
    const currentResources = Number(this.resources) || 0;
    const currentRevenue = this.totalRevenue || 0;

    this.percentageChangeRevenue = this.getPercentageChange(
      this.lastYearRevenue,
      currentRevenue
    );
    this.percentageChangeProjects = this.getPercentageChange(
      this.lastYearProjectsCount,
      currentProjects
    );
    this.percentageChangeResources = this.getPercentageChange(
      this.lastYearResources,
      currentResources
    );
  }

  projectStats = {
    completed: 40,
    inProgress: 30,
    notStarted: 30,
  };

  projectStatusData = [
    { label: 'Completed', percentage: 45, color: 'green' },
    { label: 'Ongoing', percentage: 35, color: 'orange' },
    { label: 'Delayed', percentage: 20, color: 'firebrick' },
  ];

  icons = {
    revenue: 'bx bx-money',
    projects: 'bx bxs-folder',
    timeSpent: 'bx bx-time-five',
    resources: 'bx bx-user',
  };

  onFilterChange(filter: any): void {
    console.log('Filtered', filter);
  }

  // Méthode utilitaire pour calculer le % de changement entre deux valeurs
  getPercentageChange(previous: number, current: number): string {
    if (previous === 0) return 'N/A'; // éviter division par zéro
    const change = ((current - previous) / previous) * 100;
    const sign = change >= 0 ? '+' : '';
    return `${sign}${change.toFixed(1)}%`;
  }
}
