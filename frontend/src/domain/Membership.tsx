import { MembershipStatus } from "./enumerations/Enumerations";
import { Project } from "./Project";
import { Role } from "./Role";
import { User } from "./User";

export interface Membership {
  id: number;
  createdAt: string;
  startedAt: string | null;
  endedAt: string | null;
  status: MembershipStatus;
  member: User;
  inviter: User | null;
  project: Project;
  role: Role;
}
