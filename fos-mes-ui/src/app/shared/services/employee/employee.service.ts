import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Page } from '../../models/page.model';
import { EmployeeResponse } from '../../models/employee-response.model';
import { EmployeeRequest } from '../../models/employee-request.model';
import { Position } from '../../enums/position.enum';
import { KeycloakService } from 'keycloak-angular';

@Injectable({
  providedIn: 'root',
})
export class EmployeeService {
  private baseUrl = 'http://localhost:8081/api/employees';

  constructor(private http: HttpClient, private keycloak: KeycloakService) {}

  getAll(page: number, size: number): Observable<Page<any>> {
    return this.http.get<Page<any>>(
      `${this.baseUrl}?page=${page}&size=${size}`
    );
  }

  // POST add new employee
  addEmployee(request: EmployeeRequest): Observable<EmployeeResponse> {
    return this.http.post<EmployeeResponse>(this.baseUrl, request);
  }

  // Search employees by position or keyword
  search(
    position?: Position,
    keyword?: string
  ): Observable<EmployeeResponse[]> {
    let params = new HttpParams();
    if (position) params = params.set('position', position);
    if (keyword) params = params.set('keyword', keyword);

    return this.http.get<EmployeeResponse[]>(`${this.baseUrl}/search`, {
      params,
    });
  }

  getEmployeeCount(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/count`);
  }

  // Get CNSS file by filename
  getEmployeeCnssFile(filename: string): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/cnss/${filename}`, {
      responseType: 'blob',
    });
  }

  // Get CIN file by filename
  getEmployeeCinFile(filename: string): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/cin/${filename}`, {
      responseType: 'blob',
    });
  }

  // Remplace getImageUrl par une méthode qui récupère le blob avec auth
  getImage(filename: string): Observable<Blob> {
    return this.http.get(
      `http://localhost:8081/api/employees/image/${filename}`,
      {
        responseType: 'blob', // on attend un fichier binaire
        headers: {
          Authorization: `Bearer ${this.keycloak.getToken()}`, // ajoute ton token Keycloak ici
        },
      }
    );
  }
}
