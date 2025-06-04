import { TypePhase } from '../enums/phase-type.enum';

export interface PhaseRequest {
  title: string;
  description: string;
  startDate: string;
  endDate: string;
  projectId: number;
  type: TypePhase;
  status: 'NOT_STARTED' | 'IN_PROGRESS' | 'REVIEW' | 'COMPLETED';
  budget: number;
}
