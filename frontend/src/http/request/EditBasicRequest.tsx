import { Sex } from "../response/User";

export interface EditBasicRequest {
  firstName: string;
  lastName: string;
  sex: Sex;
}
