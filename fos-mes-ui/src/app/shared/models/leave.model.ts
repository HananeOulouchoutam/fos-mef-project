import { LeaveStatus } from '../enums/leave-status.enum';

export interface Employee {
  id: number;
  firstName: string;
  lastName: string;
}

export interface Leave {
  id: number;
  startDate: string;
  endDate: string;
  reason: string;
  status: LeaveStatus;
  employeeId?: number;
  employee?: Employee;
}
