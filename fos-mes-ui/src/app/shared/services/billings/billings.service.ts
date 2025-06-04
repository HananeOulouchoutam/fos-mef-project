import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FacturationRequest } from '../../models/facturation-request.model';

@Injectable({
  providedIn: 'root',
})
export class BillingsService {
  private apiUrl = 'http://localhost:8086/api/facturations';

  constructor(private http: HttpClient) {}

  // Créer une nouvelle facturation avec FacturationRequest
  createFacturation(facturationRequest: FacturationRequest): Observable<any> {
    return this.http.post<any>(this.apiUrl, facturationRequest);
  }

  // Récupérer toutes les facturations
  getAllFacturations(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  // Récupérer une facturation par ID
  getFacturationById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  // Supprimer une facturation par ID
  deleteFacturation(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Générer l'URL pour afficher le PDF dans le navigateur
  getPdfUrl(filename: string): string {
    return `${this.apiUrl}/pdf/${filename}`;
  }
}
