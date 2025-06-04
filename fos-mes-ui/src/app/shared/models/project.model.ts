import { ProjectStatus } from '../enums/project-status.enum';
import { Phase } from './phase.model';

// export interface Project {
//   id: string;
//   title: string;
//   status: ProjectStatus;
//   progress: number;
//   startDate: string;
//   endDate: string;
//   description: string;
// }
export interface Project {
  id: number;
  title: string;
  status: ProjectStatus;
  description: string;
  startDate: string;
  duration: number;
  totalBudget: number;
  client: {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    phoneNumber: string;
    imageUrl: string;
  };
  progress: number;
  phases: Phase[]; // ✅ Ajoute cette ligne
  team: any[]; // adapte selon ton modèle
}
export interface ProjectSummary {
  completed: number;
  delayed: number;
  ongoing: number;
  total: number;
}
