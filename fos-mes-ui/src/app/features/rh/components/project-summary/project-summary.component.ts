import { Component, Input } from '@angular/core';
import { Project } from '../../../../shared/models/project.model';

@Component({
  selector: 'app-project-summary',
  standalone: false,
  templateUrl: './project-summary.component.html',
  styleUrl: './project-summary.component.css',
})
export class ProjectSummaryComponent {
  @Input() projects: Project[] = [];

  getProjectManagerName(project: Project): string {
    const manager = project.team?.find(
      (member) => member.position === 'PROJECT_MANAGER'
    );
    return manager ? `${manager.firstName} ${manager.lastName}` : 'N/A';
  }

  getDisplayStatus(progress: number): string {
    if (progress === 0) return 'N.commencé';
    if (progress === 100) return 'Terminé';
    return 'En cours';
  }

  getStatusClass(progress: number): string {
    const status = this.getDisplayStatus(progress);
    switch (status) {
      case 'En cours':
        return 'in-progress';
      case 'Terminé':
        return 'completed';
      case 'N.commencé':
        return 'not-started';
      default:
        return '';
    }
  }
}
