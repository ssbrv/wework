export interface Role {
  id: number;
  name: string;
  power: number;
  description: string | null;
  // authorizations: Authorization[]
}
