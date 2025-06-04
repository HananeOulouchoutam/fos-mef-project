export interface FacturationRequest {
  clientId?: number;
  clientName: string;
  clientEmail: string;
  clientPhoneNumber: string;
  projectId: number;
  projectTitle: string;
  phaseId?: number;
  phaseTitle: string;
  startDate: string;
  endDate: string;
  amountHT: number;
  tva: number;
}
