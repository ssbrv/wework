package cz.cvut.fit.sabirdan.wework.http.request;

import cz.cvut.fit.sabirdan.wework.domain.enumeration.MembershipStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeMembershipStatusRequest {
    MembershipStatus status;
}
