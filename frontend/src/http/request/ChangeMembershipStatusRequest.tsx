import { MembershipStatus } from "../../domain/enumerations/Enumerations";

export interface ChangeMembershipStatusRequest {
  status: MembershipStatus | undefined;
}
