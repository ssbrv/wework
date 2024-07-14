package cz.cvut.fit.sabirdan.wework.service.status.membership;

import cz.cvut.fit.sabirdan.wework.domain.status.membership.MembershipStatus;
import cz.cvut.fit.sabirdan.wework.service.status.StatusService;

public interface MembershipStatusService extends StatusService<MembershipStatus> {
    void initializeMembershipStatuses();
}
