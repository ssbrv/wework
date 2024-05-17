export type Sex = "MALE" | "FEMALE" | "UNSPECIFIED" | null;

export interface User {
  username: string;
  firstName: string;
  lastName: string;
  sex: Sex;
}
