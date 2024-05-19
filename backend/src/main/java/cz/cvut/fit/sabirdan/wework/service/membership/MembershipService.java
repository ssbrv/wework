package cz.cvut.fit.sabirdan.wework.service.membership;

import cz.cvut.fit.sabirdan.wework.domain.Membership;
import cz.cvut.fit.sabirdan.wework.http.request.ChangeMembershipStatusRequest;
import cz.cvut.fit.sabirdan.wework.http.request.InviteRequest;
import cz.cvut.fit.sabirdan.wework.http.response.InviteResponse;
import cz.cvut.fit.sabirdan.wework.service.CrudService;

import java.util.Optional;

public interface MembershipService extends CrudService<Membership> {
    InviteResponse invite(InviteRequest inviteRequest);

    Optional<Membership> findMembershipByProjectIdAndUsername(Long projectId, String username);

    Membership saveOrSubstituteMembership(Membership membership);

    void changeMembershipStatus(Long membershipId, ChangeMembershipStatusRequest changeMembershipStatusRequest);
}
