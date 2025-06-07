import { Component } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-navbar',
  standalone: false,
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
})
export class NavbarComponent {
  constructor(private keycloakService: KeycloakService) {}

  async logout() {
    await this.keycloakService.logout('http://localhost:4200/rh');
  }
}
