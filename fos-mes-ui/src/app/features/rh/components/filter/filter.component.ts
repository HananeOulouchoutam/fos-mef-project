import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-filter',
  standalone: false,
  templateUrl: './filter.component.html',
  styleUrl: './filter.component.css',
})
export class FilterComponent {
  // @Output() filterChange = new EventEmitter<{
  //   project: string;
  //   manager: string;
  //   status: string;
  // }>();

  // onFilterChange(project: string, manager: string, status: string) {
  //   this.filterChange.emit({ project, manager, status });
  // }

  selectedProject = '';
  selectedManager = '';
  selectedStatus = '';

  @Output() filterChange = new EventEmitter<{
    project: string;
    manager: string;
    status: string;
  }>();

  // Appelé quand le projet change
  onProjectChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;
    this.selectedProject = value;
    this.emitFilterChange();
  }

  // Appelé quand le manager change
  onManagerChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;
    this.selectedManager = value;
    this.emitFilterChange();
  }

  // Appelé quand le statut change
  onStatusChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;
    this.selectedStatus = value;
    this.emitFilterChange();
  }

  private emitFilterChange(): void {
    this.filterChange.emit({
      project: this.selectedProject,
      manager: this.selectedManager,
      status: this.selectedStatus,
    });
  }
}
