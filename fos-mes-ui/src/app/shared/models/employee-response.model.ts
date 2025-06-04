import { AccountStatus } from '../enums/account-status.enum';
import { Position } from '../enums/position.enum';

export interface EmployeeResponse {
  id: number;
  firstName: string;
  lastName: string;
  cnss: string;
  email: string;
  phoneNumber: string;
  position: Position;
  createdAt: string;
  status: AccountStatus;
  usedVacationDays: number;
  competencies: any[];
}
