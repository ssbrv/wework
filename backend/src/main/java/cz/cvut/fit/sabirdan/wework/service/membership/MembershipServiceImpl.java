package cz.cvut.fit.sabirdan.wework.service.membership;

import cz.cvut.fit.sabirdan.wework.domain.Membership;
import cz.cvut.fit.sabirdan.wework.domain.Project;
import cz.cvut.fit.sabirdan.wework.domain.User;
import cz.cvut.fit.sabirdan.wework.domain.role.MemberRole;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.Authorization;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.MembershipStatus;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.ProjectStatus;
import cz.cvut.fit.sabirdan.wework.http.exception.BadRequestException;
import cz.cvut.fit.sabirdan.wework.http.exception.NotFoundException;
import cz.cvut.fit.sabirdan.wework.http.exception.UnauthorizedException;
import cz.cvut.fit.sabirdan.wework.http.request.ChangeMembershipStatusRequest;
import cz.cvut.fit.sabirdan.wework.http.request.InviteRequest;
import cz.cvut.fit.sabirdan.wework.http.response.InviteResponse;
import cz.cvut.fit.sabirdan.wework.repository.MembershipRepository;
import cz.cvut.fit.sabirdan.wework.repository.ProjectRepository;
import cz.cvut.fit.sabirdan.wework.service.CrudServiceImpl;
import cz.cvut.fit.sabirdan.wework.service.project.ProjectService;
import cz.cvut.fit.sabirdan.wework.service.role.member.MemberRoleService;
import cz.cvut.fit.sabirdan.wework.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MembershipServiceImpl extends CrudServiceImpl<Membership> implements MembershipService {
    private final MembershipRepository membershipRepository;
    private final UserService userService;
    private final MemberRoleService memberRoleService;
    private final ProjectRepository projectRepository;

    @Override
    public JpaRepository<Membership, Long> getRepository() {
        return membershipRepository;
    }

    @Override
    public String getEntityName() {
        return "Membership";
    }

    @Override
    public InviteResponse invite(InviteRequest inviteRequest) {
        User inviter = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        Project project = projectRepository.findById(inviteRequest.getProjectId())
                .orElseThrow(() -> new NotFoundException("Project does not exist"));

        if (project.getStatus() != ProjectStatus.ENABLED)
            throw new BadRequestException("You cannot invite people to a closed project");

        MemberRole role = memberRoleService.findByName(inviteRequest.getRoleName())
                .orElseThrow(() -> new NotFoundException("roleName", "Role does not exist. Please, contact tech support"));

        User user = userService.getByUsername(inviteRequest.getUsername());

        Membership membership = new Membership(
                user,
                project,
                inviter,
                role
        );

        if (inviter.isAuthorized(Authorization.SYSTEM_INVITE))
            return new InviteResponse(saveOrSubstituteMembership(membership).getId());

        Membership inviterMembership = findMembershipByProjectIdAndUsername(
                inviteRequest.getProjectId(),
                inviter.getUsername()
        ).orElseThrow(() -> new UnauthorizedException("You are not a part of this project"));

        if (user.getUsername().equals(inviter.getUsername()))
            throw new BadRequestException("username", "You selected yourself, but you are already in this project. Why would you invite yourself?");

        if (!inviterMembership.isAuthorized(Authorization.INVITE))
            throw new UnauthorizedException("You are not authorized to invite other people to this project");

        if (!inviterMembership.hasAuthorityOver(membership))
            throw new UnauthorizedException("roleName", "You are not authorized to select this role");

        return new InviteResponse(saveOrSubstituteMembership(membership).getId());
    }

    @Override
    public Optional<Membership> findMembershipByProjectIdAndUsername(Long projectId, String username) {
        return membershipRepository.findMembershipByProjectIdAndUsername(projectId, username);
    }

    @Override
    public Membership saveOrSubstituteMembership(Membership membership) {
        Optional<Membership> optionalMembership = findMembershipByProjectIdAndUsername(
                membership.getProject().getId(),
                membership.getMember().getUsername()
        );

        optionalMembership.ifPresent(membershipToSubstitute -> deleteById(membershipToSubstitute.getId()));
        return save(membership);
    }

    @Override
    public void changeMembershipStatus(Long membershipId, ChangeMembershipStatusRequest changeMembershipStatusRequest) {
        User editor = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new NotFoundException("Membership does not exist"));
        User member = membership.getMember();
        Optional<Membership> optionalEditorMembership = findMembershipByProjectIdAndUsername(membership.getProject().getId(), editor.getUsername());

        final boolean hasMemberAuthority = optionalEditorMembership.map(membershipEditor ->
                        (membershipEditor.isAuthorized(Authorization.KICK) && membershipEditor.hasAuthorityOver(membership)))
                .orElse(false);
        final boolean hasSystemAuthority = editor.isAuthorized(Authorization.SYSTEM_CHANGE_MEMBERSHIP_STATUS) && editor.hasAuthorityOver(member);
        final boolean selfChange = editor.getUsername().equals(changeMembershipStatusRequest.getUsername());

        if ((hasMemberAuthority || hasSystemAuthority)
                && (membership.getStatus() == MembershipStatus.ENABLED || membership.getStatus() == MembershipStatus.PROPOSED)
                && changeMembershipStatusRequest.getStatus() == MembershipStatus.KICKED) {
            membership.kick();
            return;
        }

        if (!selfChange && !hasMemberAuthority)
            throw new UnauthorizedException("You are not authorized to perform this action");

        if (membership.getStatus() == MembershipStatus.PROPOSED && changeMembershipStatusRequest.getStatus() == MembershipStatus.ENABLED) {
            membership.accept();
            return;
        }

        if (!selfChange)
            throw new BadRequestException("Invalid operation: cannot change membership status from " + membership.getStatus() + " to " + changeMembershipStatusRequest.getStatus());

        if (membership.getStatus() == MembershipStatus.PROPOSED && changeMembershipStatusRequest.getStatus() == MembershipStatus.REJECTED) {
            membership.reject();
            return;
        }

        if (membership.getStatus() == MembershipStatus.ENABLED && changeMembershipStatusRequest.getStatus() == MembershipStatus.LEFT) {
            membership.leave();
            return;
        }

        throw new BadRequestException("Invalid operation: cannot self change membership status from " + membership.getStatus() + " to " + changeMembershipStatusRequest.getStatus());
    }
}
