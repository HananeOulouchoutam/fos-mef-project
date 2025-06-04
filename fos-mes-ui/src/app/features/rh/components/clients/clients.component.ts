import { Component, OnInit, AfterViewInit } from '@angular/core';
import {
  Position,
  PositionLabels,
} from '../../../../shared/enums/position.enum';
import { ClientService } from '../../../../shared/services/client/client.service';
import * as FileSaver from 'file-saver';
import ExcelJS from 'exceljs';

declare var bootstrap: any;

@Component({
  selector: 'app-clients',
  standalone: false,
  templateUrl: './clients.component.html',
  styleUrl: './clients.component.css',
})
export class ClientsComponent implements OnInit, AfterViewInit {
  clients!: any[];
  currentPage: number = 0;
  pageSize: number = 5;
  totalPages: number = 0;
  pageNumbers: number[] = [];
  showSearch = false;
  searchTerm = '';
  isEditMode: boolean = false;
  positions = Object.values(Position);
  positionLabels = PositionLabels;
  private clientModal: any;
  showForm = false;
  isDownloading: boolean = false;
  clientAddedToast: any;
  selectedClient: any | null = null;

  constructor(private clientService: ClientService) {}

  ngOnInit(): void {
    this.loadClients();
  }

  ngAfterViewInit() {
    const toastEl = document.getElementById('successToast');
    if (toastEl) {
      this.clientAddedToast = new bootstrap.Toast(toastEl);
    } else {
      console.error('Toast element not found!');
    }
  }

  loadClients() {
    this.clientService.getClients(this.currentPage, this.pageSize).subscribe({
      next: (response) => {
        const content = response?.content || [];
        console.log('content', content);
        this.clients = content.map((employee: any) => ({
          ...employee,
          showMenu: false,
        }));
        this.totalPages = response?.totalPages || 0;
        this.updatePageNumbers();
      },
      error: (err) => {
        console.error('Erreur lors du chargement des clients:', err);
      },
    });
  }

  // Function to update the page numbers based on totalPages
  updatePageNumbers() {
    this.pageNumbers = Array.from({ length: this.totalPages }, (_, i) => i);
  }

  changePage(page: number) {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadClients();
    }
  }
  newClient = {
    id: 0,
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    projectId: 0,
    report: '',
  };

  openModal() {
    const modalElement = document.getElementById('clientModal');
    this.clientModal = new bootstrap.Modal(modalElement);
    this.clientModal.show();
  }

  submitForm(form: any): void {
    if (form.invalid) {
      return;
    }
    if (this.isEditMode) {
      const clientToUpdate = {
        firstName: this.newClient.firstName,
        lastName: this.newClient.lastName,
        email: this.newClient.email,
        phoneNumber: this.newClient.phoneNumber,
        projectId: this.newClient.projectId,
        report: this.newClient.report,
      };
      this.clientService
        .updateClient(this.newClient.id, clientToUpdate)
        .subscribe(
          (response) => {
            console.log('Client mis à jour avec succès:', response);
            this.loadClients();
            this.cancelForm();
            this.resetForm();
            // Changer le texte du toast avant de l'afficher
            const toastBody = document.querySelector(
              '#successToast .toast-body'
            );
            if (toastBody) {
              toastBody.textContent = 'Client modifié avec succès !';
            }
            this.clientAddedToast.show();
          },
          (error) => {
            console.error("Erreur lors de la mise à jour de l'employé:", error);
          }
        );
    } else {
      const clientToAdd = {
        firstName: this.newClient.firstName,
        lastName: this.newClient.lastName,
        email: this.newClient.email,
        phoneNumber: this.newClient.phoneNumber,
        projectId: this.newClient.projectId,
        report: this.newClient.report,
      };
      this.clientService.createClient(clientToAdd).subscribe(
        (response) => {
          console.log('Nouvel employé ajouté avec succès:', response);
          this.loadClients();
          this.cancelForm();
          this.resetForm();
          const toastBody = document.querySelector('#successToast .toast-body');
          if (toastBody) {
            toastBody.textContent = 'Client ajouté avec succès !';
          }
          this.clientAddedToast.show();
        },
        (error) => {
          console.error("Erreur lors de l'ajout de l'employé:", error);
        }
      );
    }
  }

  resetForm() {
    this.newClient = {
      id: 0,
      firstName: '',
      lastName: '',
      email: '',
      phoneNumber: '',
      projectId: 0,
      report: '',
    };
    this.isEditMode = false;
  }

  toggleSearchBar(): void {
    if (this.showSearch == true) {
      this.loadClients();
      this.searchTerm = '';
    }
    this.showSearch = !this.showSearch;
  }

  toggleMenu(employee: any) {
    this.clients.forEach((emp) => {
      if (emp !== employee) emp.showMenu = false;
    });
    employee.showMenu = !employee.showMenu;
  }

  editClient(client: any) {
    this.newClient = { ...client };
    this.isEditMode = true;
    this.openForm();
  }

  deleteEmployee(employee: any) {
    console.log('Supprimer', employee);
  }

  searchEmployees() {
    const keyword = this.searchTerm.trim();

    if (!keyword) {
      this.loadClients();
      return;
    }

    this.clientService.searchClients(keyword).subscribe({
      next: (response) => {
        this.clients = response;
        this.totalPages = 1;
        this.pageNumbers = [0];
        this.currentPage = 0;
      },
      error: (err) => {
        console.error('Erreur lors de la recherche des employés:', err);
      },
    });
  }

  getImageUrl(filename: string): string {
    return `http://localhost:8082/api/clients/image/${filename}`;
  }

  openForm() {
    this.showForm = true;
  }

  cancelForm() {
    this.resetForm();
    this.showForm = false;
  }

  async downloadExcel() {
    const workbook = new ExcelJS.Workbook();
    const worksheet = workbook.addWorksheet('Clients');

    // Définir les colonnes
    worksheet.columns = [
      { header: 'Full Name', key: 'fullName', width: 30 },
      { header: 'Téléphone', key: 'phoneNumber', width: 20 },
      { header: 'Email', key: 'email', width: 30 },
    ];

    // Ajouter les lignes avec les données des clients
    this.clients.forEach((client) => {
      worksheet.addRow({
        fullName: `${client.firstName} ${client.lastName}`,
        phoneNumber: client.phoneNumber,
        email: client.email,
      });
    });

    // Style de l'entête
    worksheet.getRow(1).font = { bold: true, color: { argb: 'FFFFFFFF' } };
    worksheet.getRow(1).fill = {
      type: 'pattern',
      pattern: 'solid',
      fgColor: { argb: 'FF4472C4' },
    };

    // Générer et télécharger le fichier Excel
    const buffer = await workbook.xlsx.writeBuffer();
    const blob = new Blob([buffer], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    });
    FileSaver.saveAs(blob, 'clients.xlsx');
  }

  showDetails(client: any) {
    this.selectedClient = client;
  }

  closeDetails() {
    this.selectedClient = null;
  }

  getClientFullNameLowercase(): string {
    if (!this.selectedClient) return '';
    return (
      this.selectedClient.firstName +
      '_' +
      this.selectedClient.lastName
    ).toLowerCase();
  }
}
