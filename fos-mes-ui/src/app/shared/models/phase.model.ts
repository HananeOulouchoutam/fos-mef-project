import { TypePhase } from '../enums/phase-type.enum';

export interface Phase {
  id: number;
  title: string;
  description: string;
  startdate: string;
  endDate: string;
  updatedAt: string;
  status: 'NOT_STARTED' | 'IN_PROGRESS' | 'REVIEW' | 'COMPLETED';
  type: TypePhase;
  budget: number;
}
