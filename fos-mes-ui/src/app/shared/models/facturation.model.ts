export interface Facturation {
  id?: number;
  projectId: number;
  phaseId?: number;
  billingDate: string;
  amount: number;
  tva: number;
  totalAmount: number;
}
