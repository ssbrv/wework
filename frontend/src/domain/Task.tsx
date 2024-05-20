import { TaskStatus } from "./enumerations/Enumerations";
import { Project } from "./Project";
import { User } from "./User";

export interface Task {
  id: number;
  summary: string;
  description: string;
  status: TaskStatus;
  project: Project;
  author: User;
  assignees: User[];
}
