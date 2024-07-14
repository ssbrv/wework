export interface Role {
  id: number;
  value: string;
  name: string;
  power: number;
  description: string | null;
  // authorizations: Authorization[]
}
