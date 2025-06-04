export interface ProjectRequest {
  title: string;
  description?: string;
  startDate: string | null;
  duration: number;
  clientId: number | null;
  employeeIds: number[];
}
