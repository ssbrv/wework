import { Project } from "./Project";
import { Role } from "./Role";
import { Status } from "./Status";
import { User } from "./User";

export interface Membership {
  id: number;
  createdAt: string;
  startedAt: string | null;
  endedAt: string | null;
  status: Status;
  member: User;
  inviter: User | null;
  project: Project;
  role: Role;
}
