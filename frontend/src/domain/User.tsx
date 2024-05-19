import { Sex } from "./enumerations/Enumerations";
import { Role } from "./Role";

export interface User {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  sex: Sex | null;
  role: Role | null;
}
