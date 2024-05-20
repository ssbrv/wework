import { ProjectStatus } from "../../domain/enumerations/Enumerations";

export interface ChangeProjectStatusRequest {
  status: ProjectStatus;
}
