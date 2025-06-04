import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Project } from '../../models/project.model';
import { ProjectRequest } from '../../models/project-request.model';

@Injectable({
  providedIn: 'root',
})
@Injectable({
  providedIn: 'root',
})
export class ProjectsService {
  private baseUrl = 'http://localhost:8085/api/projects';

  constructor(private http: HttpClient) {}

  getAllProjects(page: number = 0, size: number = 10): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<any>(this.baseUrl, { params });
  }

  getProjectById(id: number): Observable<Project> {
    return this.http.get<Project>(`${this.baseUrl}/${id}`);
  }

  createProject(request: ProjectRequest): Observable<Project> {
    return this.http.post<Project>(this.baseUrl, request);
  }

  updateProject(id: number, request: ProjectRequest): Observable<Project> {
    return this.http.put<Project>(`${this.baseUrl}/${id}`, request);
  }

  deleteProject(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  updateProgress(id: number, progress: number): Observable<Project> {
    const params = new HttpParams().set('progress', progress.toString());
    return this.http.patch<Project>(`${this.baseUrl}/${id}/progress`, null, {
      params,
    });
  }
}
