import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PhaseRequest } from '../../models/phase-request.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PhasesService {
  private apiUrl = 'http://localhost:8085/api/phases';

  constructor(private http: HttpClient) {}

  addPhase(phaseRequest: PhaseRequest): Observable<any> {
    return this.http.post<any>(this.apiUrl, phaseRequest);
  }

  deletePhase(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  updatePhase(id: number, phaseRequest: PhaseRequest): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}/edit`, phaseRequest);
  }
}
