import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Leave } from '../../../../shared/models/leave.model';
import { LeavesService } from '../../../../shared/services/leaves/leaves.service';
import { LeaveStatus } from '../../../../shared/enums/leave-status.enum';

declare var bootstrap: any;
@Component({
  selector: 'app-leaves',
  standalone: false,
  templateUrl: './leaves.component.html',
  styleUrl: './leaves.component.css',
})
export class LeavesComponent implements OnInit {
  selectedLeave: any = null;
  showCalendar = false;
  leaves: Leave[] = [];
  paginatedLeaves: Leave[] = [];
  leaveStatus = LeaveStatus;
  showSearch = false;
  searchTerm = '';

  // Pagination
  currentPage: number = 1;
  itemsPerPage: number = 5;
  totalPages: number = 1;
  statusFilter: LeaveStatus | '' = '';
  filteredLeaves: Leave[] = [];

  constructor(private leavesService: LeavesService) {}

  ngOnInit(): void {
    this.getLeaves();
  }

  // getLeaves() {
  //   this.leavesService
  //     .getLeavesOrderedByStatus(this.currentPage - 1, this.itemsPerPage)
  //     .subscribe({
  //       next: (data) => {
  //         this.paginatedLeaves = data.content;
  //         this.totalPages = data.totalPages;
  //         console.log('Page:', this.currentPage, 'Contenu:', data.content);
  //       },
  //       error: (err) => {
  //         console.error('Erreur lors du chargement des congés', err);
  //       },
  //     });
  // }

  getLeaves() {
    this.leavesService
      .getLeavesOrderedByStatus(this.currentPage - 1, this.itemsPerPage)
      .subscribe({
        next: (data) => {
          this.leaves = data.content; // toutes les données de la page
          this.filteredLeaves = [...this.leaves]; // copie pour filtre
          this.totalPages = data.totalPages;
          this.updatePaginatedLeaves(); // affiche la page
        },
        error: (err) => {
          console.error('Erreur lors du chargement des congés', err);
        },
      });
  }

  // updatePaginatedLeaves() {
  //   const startIndex = (this.currentPage - 1) * this.itemsPerPage;
  //   const endIndex = startIndex + this.itemsPerPage;
  //   this.paginatedLeaves = this.leaves.slice(startIndex, endIndex);
  // }

  updatePaginatedLeaves() {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.paginatedLeaves = this.filteredLeaves.slice(startIndex, endIndex);
  }

  changePage(page: number) {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      this.getLeaves();
    }
  }
  openDetails(demande: any) {
    this.selectedLeave = demande;
  }

  openCalendar() {
    this.showCalendar = true;
  }

  closeCalendar() {
    this.showCalendar = false;
  }

  confirmLeave() {
    if (this.selectedLeave) {
      if (this.selectedLeave) {
        this.leavesService
          .updateLeaveStatus(this.selectedLeave.id, LeaveStatus.APPROVED)
          .subscribe({
            next: (updatedLeave) => {
              // this.selectedLeave.status = updatedLeave.status;
              this.selectedLeave = false;
              this.showSuccessToast();
              this.getLeaves();
            },
            error: (err) => {
              console.error('Erreur lors de la confirmation du congé', err);
            },
          });
      }
    }
  }

  rejectLeave() {
    if (this.selectedLeave) {
      this.leavesService
        .updateLeaveStatus(this.selectedLeave.id, LeaveStatus.REJECTED)
        .subscribe({
          next: (updatedLeave) => {
            this.selectedLeave = false;
            this.getLeaves();
          },
          error: (err) => {
            console.error('Erreur lors du rejet du congé', err);
          },
        });
    }
  }

  getStatusLabel(status: LeaveStatus): string {
    switch (status) {
      case LeaveStatus.PENDING:
        return 'En attente';
      case LeaveStatus.APPROVED:
        return 'Confirmé';
      case LeaveStatus.REJECTED:
        return 'Rejeté';
      default:
        return status;
    }
  }
  goBack() {
    this.selectedLeave = false;
  }
  toggleSearchBar(): void {
    if (this.showSearch == true) {
      this.getLeaves();
    }
    this.showSearch = !this.showSearch;
  }

  searchLeaves() {
    const term = this.searchTerm.toLowerCase().trim();

    if (term === '') {
      this.getLeaves();
      return;
    }

    this.paginatedLeaves = this.paginatedLeaves.filter((leave) => {
      const fullName = `${leave.employee?.firstName ?? ''} ${
        leave.employee?.lastName ?? ''
      }`.toLowerCase();
      return fullName.includes(term);
    });
  }

  filterLeaves() {
    if (this.statusFilter === '') {
      this.filteredLeaves = [...this.leaves];
    } else {
      this.filteredLeaves = this.leaves.filter(
        (leave) => leave.status === this.statusFilter
      );
    }
    this.currentPage = 1; // reset pagination à la 1ère page
    this.totalPages = Math.ceil(this.filteredLeaves.length / this.itemsPerPage);
    this.updatePaginatedLeaves();
  }

  showSuccessToast() {
    const toastEl = document.getElementById('successToast');
    if (toastEl) {
      const toast = new bootstrap.Toast(toastEl);
      toast.show();
    }
  }
}
