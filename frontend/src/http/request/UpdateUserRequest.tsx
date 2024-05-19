import { Sex } from "../../domain/enumerations/Enumerations";

export interface UpdateUserRequest {
  username: string;
  firstName: string;
  lastName: string;
  sex: Sex;
}
