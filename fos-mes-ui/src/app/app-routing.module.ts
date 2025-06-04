import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RhComponent } from './features/rh/rh.component';
import { CommonModule } from '@angular/common';
import { RhDashboardComponent } from './features/rh/components/rh-dashboard/rh-dashboard.component';
import { EmployeeComponent } from './features/rh/components/employee/employee.component';
import { LeavesComponent } from './features/rh/components/leaves/leaves.component';
import { ProjectsComponent } from './features/rh/components/projects/projects.component';
import { EditProfilComponent } from './features/rh/components/edit-profil/edit-profil.component';
import { TaskComponent } from './features/rh/components/task/task.component';
import { ClientsComponent } from './features/rh/components/clients/clients.component';
import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  {
    path: 'rh',
    component: RhComponent,
    children: [
      { path: '', component: RhDashboardComponent },
      { path: 'employee', component: EmployeeComponent },
      { path: 'leaves', component: LeavesComponent },
      { path: 'projects', component: ProjectsComponent },
      { path: 'projects/tasks/:id', component: TaskComponent },
      { path: 'edit-profil', component: EditProfilComponent },
      { path: 'clients', component: ClientsComponent },
    ],
    canActivate: [AuthGuard],
    data: { roles: ['RH'] },
  },
  { path: 'employee', component: EmployeeComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule, CommonModule],
})
export class AppRoutingModule {}
