import { Project } from "./Project";
import { Status } from "./Status";
import { User } from "./User";

export interface Task {
  id: number;
  summary: string;
  description: string;
  status: Status;
  project: Project;
  author: User;
  assignees: User[];
}
