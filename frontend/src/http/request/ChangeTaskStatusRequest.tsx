import { TaskStatus } from "../../domain/enumerations/Enumerations";

export interface ChangeTaskStatusRequest {
  status: TaskStatus;
}
