import { Position } from '../enums/position.enum';

export interface EmployeeRequest {
  firstName: string;
  lastName: string;
  cnss: string;
  email: string;
  phoneNumber: string;
  position: Position;
}
