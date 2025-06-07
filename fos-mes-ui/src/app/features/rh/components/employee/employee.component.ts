import { AfterViewInit, Component, OnInit } from '@angular/core';
import {
  Position,
  PositionLabels,
} from '../../../../shared/enums/position.enum';
import { EmployeeRequest } from '../../../../shared/models/employee-request.model';
import { EmployeeService } from '../../../../shared/services/employee/employee.service';
import { NgForm } from '@angular/forms';
import * as FileSaver from 'file-saver';
import ExcelJS from 'exceljs';
import { Router } from '@angular/router';

declare var bootstrap: any;

@Component({
  selector: 'app-employee',
  standalone: false,
  templateUrl: './employee.component.html',
  styleUrls: ['./employee.component.css'],
})
export class EmployeeComponent implements OnInit, AfterViewInit {
  employees!: any[];
  currentPage: number = 0;
  pageSize: number = 5;
  totalPages: number = 0;
  pageNumbers: number[] = [];
  showSearch = false;
  searchTerm = '';
  isEditMode: boolean = false;
  positions = Object.values(Position);
  positionLabels = PositionLabels;
  employeeAddedToast: any;
  selectedEmployeeDetails: any = null;
  showDetailsDiv: boolean = false;
  showDetailsModal: any;
  cnssFileUrl: string | null = null;
  toastMessage: string = '';
  showForm: boolean = false;
  isDownloading: boolean = false;

  constructor(
    private employeeService: EmployeeService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadEmployees();
  }

  ngAfterViewInit() {
    const toastEl = document.getElementById('successToast');
    this.employeeAddedToast = new bootstrap.Toast(toastEl);
  }
  loadEmployees() {
    this.employeeService.getAll(this.currentPage, this.pageSize).subscribe({
      next: (response) => {
        const content = response?.content || [];
        console.log('content', content);
        this.employees = content.map((employee: any) => ({
          ...employee,
          showMenu: false,
        }));
        this.totalPages = response?.totalPages || 0;
        this.updatePageNumbers();
      },
      error: (err) => {
        console.error('Erreur lors du chargement des employés:', err);
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
      this.loadEmployees();
    }
  }
  newEmployee = {
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    position: '',
    cnss: '',
  };

  submitForm(form: NgForm): void {
    if (form.invalid) {
      Object.values(form.controls).forEach((control) => {
        control.markAsTouched();
      });
      return;
    }
    const employeeRequest: EmployeeRequest = {
      firstName: this.newEmployee.firstName,
      lastName: this.newEmployee.lastName,
      email: this.newEmployee.email,
      phoneNumber: this.newEmployee.phoneNumber,
      cnss: this.newEmployee.cnss,
      position: Position[this.newEmployee.position as keyof typeof Position],
    };

    if (this.isEditMode) {
      console.log('Modifier employé:', this.newEmployee);
      this.toastMessage = 'Employé modifié avec succès !';
      this.loadEmployees();
      this.resetForm();
      this.showForm = false;
      this.showToast();
    } else {
      this.employeeService.addEmployee(employeeRequest).subscribe(
        (response) => {
          this.toastMessage = 'Employé ajouté avec succès !';
          this.loadEmployees();
          this.resetForm();
          this.showForm = false;
          this.showToast();
        },
        (error) => {
          console.error("Erreur lors de l'ajout de l'employé:", error);
        }
      );
    }
  }

  resetForm() {
    this.newEmployee = {
      firstName: '',
      lastName: '',
      email: '',
      phoneNumber: '',
      position: 'developpeur',
      cnss: '',
    };
    this.isEditMode = false;
  }

  toggleSearchBar(): void {
    if (this.showSearch == true) {
      this.loadEmployees();
    }
    this.showSearch = !this.showSearch;
  }

  toggleMenu(employee: any) {
    this.employees.forEach((emp) => {
      if (emp !== employee) emp.showMenu = false;
    });
    employee.showMenu = !employee.showMenu;
  }

  editEmployee(employee: any) {
    this.openForm(true, employee);
  }

  deleteEmployee(employee: any) {
    console.log('Supprimer', employee);
  }

  searchEmployees() {
    const keyword = this.searchTerm.trim();

    if (!keyword) {
      this.loadEmployees();
      return;
    }

    this.employeeService.search(undefined, keyword).subscribe({
      next: (response) => {
        this.employees = response;
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
    return `http://localhost:8081/api/employees/image/${filename}`;
  }

  showEmployeeDetails(employee: any) {
    this.selectedEmployeeDetails = employee;
    this.showDetailsDiv = true;
    // const modalElement = document.getElementById('employeeDetailsModal');
    // this.showDetailsModal = new bootstrap.Modal(modalElement);
    // this.showDetailsModal.show();
  }

  getSelectedEmployeePositionLabel(): string {
    if (
      !this.selectedEmployeeDetails ||
      !this.selectedEmployeeDetails.position
    ) {
      return '';
    }

    // Si position est une clé de positionLabels, retourne la valeur
    const pos = this.selectedEmployeeDetails
      .position as keyof typeof PositionLabels;
    return this.positionLabels[pos] || '';
  }

  getEmployeeFullNameLowercase(): string {
    const firstName = this.selectedEmployeeDetails?.firstName ?? '';
    const lastName = this.selectedEmployeeDetails?.lastName ?? '';
    return (firstName + lastName).toLowerCase();
  }

  openForm(isEdit: boolean = false, employee: any = null) {
    this.isEditMode = isEdit;
    if (isEdit && employee) {
      this.newEmployee = { ...employee };
    } else {
      this.newEmployee = {
        firstName: '',
        lastName: '',
        cnss: '',
        email: '',
        phoneNumber: '',
        position: '',
      };
    }
    this.showForm = true;
  }

  cancelForm() {
    this.showForm = false;
    this.newEmployee = {
      firstName: '',
      lastName: '',
      email: '',
      phoneNumber: '',
      position: '',
      cnss: '',
    };
  }
  closeDetails() {
    this.selectedEmployeeDetails = null;
    this.showDetailsDiv = false;
  }
  goBack() {
    this.showDetailsDiv = false;
  }

  async downloadExcel() {
    const workbook = new ExcelJS.Workbook();
    const worksheet = workbook.addWorksheet('Employés');

    // Ajouter les colonnes avec header + largeur
    worksheet.columns = [
      { header: 'Full Name', key: 'fullName', width: 30 },
      { header: "Date d'embauche", key: 'hireDate', width: 20 },
      { header: 'Poste', key: 'position', width: 20 },
      { header: 'Email', key: 'email', width: 30 },
      { header: 'Téléphone', key: 'phone', width: 20 },
    ];

    // Ajouter les données
    this.employees.forEach((emp) => {
      worksheet.addRow({
        fullName: emp.firstName + ' ' + emp.lastName,
        hireDate: emp.hireDate,
        position:
          this.positionLabels[
            emp.position as keyof typeof this.positionLabels
          ] || '',
        email: emp.email,
        phone: emp.phoneNumber,
      });
    });

    // Style entête (1ère ligne)
    worksheet.getRow(1).font = { bold: true, color: { argb: 'FFFFFFFF' } };
    worksheet.getRow(1).fill = {
      type: 'pattern',
      pattern: 'solid',
      fgColor: { argb: 'FF4472C4' },
    };

    // Générer le buffer
    const buffer = await workbook.xlsx.writeBuffer();
    const blob = new Blob([buffer], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    });

    FileSaver.saveAs(blob, 'employes.xlsx');
  }

  showToast(): void {
    const toastEl = document.getElementById('successToast');
    if (toastEl) {
      const body = toastEl.querySelector('.toast-body');
      if (body) {
        body.textContent = this.toastMessage;
      }
      const toast = new bootstrap.Toast(toastEl);
      toast.show();
    }
  }
  onImageError(event: Event) {
    const target = event.target as HTMLImageElement;
    target.src = 'assets/images/default-avatar.png';
  }
}
