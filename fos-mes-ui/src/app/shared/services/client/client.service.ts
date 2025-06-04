import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Page } from '../../models/page.model';
import { Client } from '../../models/client.model';

@Injectable({
  providedIn: 'root',
})
export class ClientService {
  private apiUrl = 'http://localhost:8082/api/clients';

  constructor(private http: HttpClient) {}

  getClients(page: number = 0, size: number = 10): Observable<Page<Client>> {
    return this.http.get<Page<Client>>(
      `${this.apiUrl}?page=${page}&size=${size}`
    );
  }

  getClientById(id: number): Observable<Client> {
    return this.http.get<Client>(`${this.apiUrl}/${id}`);
  }

  createClient(client: Client): Observable<Client> {
    return this.http.post<Client>(this.apiUrl, client);
  }

  updateClient(id: number, client: Client): Observable<Client> {
    return this.http.put<Client>(`${this.apiUrl}/${id}`, client);
  }

  deleteClient(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  searchClients(keyword: string): Observable<Client[]> {
    return this.http.get<Client[]>(
      `${this.apiUrl}/search?keyword=${encodeURIComponent(keyword)}`
    );
  }
}
