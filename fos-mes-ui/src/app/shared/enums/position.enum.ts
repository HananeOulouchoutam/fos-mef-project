export enum Position {
  DEVELOPER = 'DEVELOPER',
  PROJECT_MANAGER = 'PROJECT_MANAGER',
  DESIGNER = 'DESIGNER',
  QA_ENGINEER = 'QA_ENGINEER',
  DEVOPS = 'DEVOPS',
  PRODUCT_OWNER = 'PRODUCT_OWNER',
}

export const PositionLabels: { [key in Position]: string } = {
  [Position.DEVELOPER]: 'Développeur',
  [Position.PROJECT_MANAGER]: 'Chef de projet',
  [Position.DESIGNER]: 'Designer',
  [Position.QA_ENGINEER]: 'Ingénieur QA',
  [Position.DEVOPS]: 'Ingénieur DevOps',
  [Position.PRODUCT_OWNER]: 'Product Owner',
};
