package cz.cvut.fit.sabirdan.wework.service.membership;

import cz.cvut.fit.sabirdan.wework.domain.Membership;
import cz.cvut.fit.sabirdan.wework.http.request.ChangeMemberRoleRequest;
import cz.cvut.fit.sabirdan.wework.http.request.ChangeMembershipStatusRequest;
import cz.cvut.fit.sabirdan.wework.http.request.InviteRequest;
import cz.cvut.fit.sabirdan.wework.http.response.InviteResponse;
import cz.cvut.fit.sabirdan.wework.http.response.membership.MembershipDTO;
import cz.cvut.fit.sabirdan.wework.service.CrudService;

import java.util.List;
import java.util.Optional;

public interface MembershipService extends CrudService<Membership> {
    InviteResponse invite(InviteRequest inviteRequest);

    Membership getById(Long id);

    Optional<Membership> findMembershipByProjectIdAndUsername(Long projectId, String username);

    Optional<Membership> findEnabledMembershipByProjectIdAndUsername(Long projectId, String username);

    Membership saveOrSubstituteMembership(Membership membership);

    void changeMembershipStatus(Long membershipId, ChangeMembershipStatusRequest changeMembershipStatusRequest);

    List<Membership> getInvitations();

    void changeMemberRole(Long membershipId, ChangeMemberRoleRequest changeMemberRoleRequest);

    Integer countEnabledMembershipsByProjectId(Long id);
}
