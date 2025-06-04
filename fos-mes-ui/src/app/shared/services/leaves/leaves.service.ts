import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LeaveStatus } from '../../enums/leave-status.enum';
import { Leave } from '../../models/leave.model';

@Injectable({
  providedIn: 'root',
})
export class LeavesService {
  private apiUrl = 'http://localhost:8083/api/leaves';

  constructor(private http: HttpClient) {}

  getLeavesOrderedByStatus(page: number, size: number): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<any>(`${this.apiUrl}`, { params });
  }

  getPendingLeaves(page: number, size: number): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<any>(`${this.apiUrl}/pending`, { params });
  }

  updateLeaveStatus(id: number, status: LeaveStatus): Observable<Leave> {
    return this.http.put<Leave>(`${this.apiUrl}/${id}/status`, null, {
      params: { status },
    });
  }

  searchLeaves(
    name?: string,
    page: number = 0,
    size: number = 5
  ): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (name) {
      params = params.set('name', name);
    }
    return this.http.get<any>(`${this.apiUrl}/search`, { params });
  }

  updateLeave(leave: Leave): Observable<Leave> {
    return this.http.put<Leave>(`${this.apiUrl}/${leave.id}`, leave);
  }

  getLeaveById(id: number): Observable<Leave> {
    return this.http.get<Leave>(`${this.apiUrl}/${id}`);
  }

  getLeaveCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }
}
