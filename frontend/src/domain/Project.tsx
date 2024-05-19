import { ProjectStatus } from "./enumerations/Enumerations";

export interface Project {
  id: number;
  name: string;
  description: string | null;
  status: ProjectStatus;
  memberCount: number;
}
