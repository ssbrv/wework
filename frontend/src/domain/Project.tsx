import { Status } from "./Status";

export interface Project {
  id: number;
  name: string;
  description: string | null;
  status: Status;
  memberCount: number;
}
