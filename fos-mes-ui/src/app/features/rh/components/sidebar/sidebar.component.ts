import { Component } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-sidebar',
  standalone: false,
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css',
})
export class SidebarComponent {
  constructor(private keycloakService: KeycloakService) {}

  async logout() {
    await this.keycloakService.logout('http://localhost:4200/rh');
  }
}
