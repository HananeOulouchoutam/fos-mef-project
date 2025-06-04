import {
  APP_INITIALIZER,
  CUSTOM_ELEMENTS_SCHEMA,
  LOCALE_ID,
  NgModule,
} from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { SidebarComponent } from './features/rh/components/sidebar/sidebar.component';
import { NavbarComponent } from './features/rh/components/navbar/navbar.component';
import { RhComponent } from './features/rh/rh.component';
import { RhDashboardComponent } from './features/rh/components/rh-dashboard/rh-dashboard.component';
import { BrowserModule } from '@angular/platform-browser';
import { EmployeeComponent } from './features/rh/components/employee/employee.component';
import { FormsModule } from '@angular/forms';
import { LeavesComponent } from './features/rh/components/leaves/leaves.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CalendarModalComponent } from './features/rh/components/calendar-modal/calendar-modal.component';
import { EditProfilComponent } from './features/rh/components/edit-profil/edit-profil.component';
import { TaskComponent } from './features/rh/components/task/task.component';
import { ProjectSummaryComponent } from './features/rh/components/project-summary/project-summary.component';
import { OverallProgressComponent } from './features/rh/components/overall-progress/overall-progress.component';
import { FilterComponent } from './features/rh/components/filter/filter.component';
import { ClientsComponent } from './features/rh/components/clients/clients.component';
import { ProjectsComponent } from './features/rh/components/projects/projects.component';
import { NgApexchartsModule } from 'ng-apexcharts';
import { LineChartComponent } from './features/rh/components/line-chart/line-chart.component';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';

function initializeKeycloak(keycloak: KeycloakService) {
  return () =>
    keycloak.init({
      config: {
        url: 'http://localhost:9090',
        realm: 'fos-mes-realm',
        clientId: 'fos-mes-ui',
      },
      initOptions: {
        onLoad: 'check-sso',
        silentCheckSsoRedirectUri:
          window.location.origin + '/assets/silent-check-sso.html',
      },
    });
}

@NgModule({
  declarations: [
    AppComponent,
    RhComponent,
    EmployeeComponent,
    SidebarComponent,
    NavbarComponent,
    RhDashboardComponent,
    LeavesComponent,
    CalendarModalComponent,
    EditProfilComponent,
    TaskComponent,
    ProjectSummaryComponent,
    OverallProgressComponent,
    FilterComponent,
    ClientsComponent,
    ProjectsComponent,
    LineChartComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    NgbModule,
    FormsModule,
    NgApexchartsModule,
    KeycloakAngularModule,
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakService],
    },
    { provide: LOCALE_ID, useValue: 'fr-MA' },
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  bootstrap: [AppComponent],
})
export class AppModule {}
