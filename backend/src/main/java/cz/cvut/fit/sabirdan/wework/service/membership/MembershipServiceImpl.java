package cz.cvut.fit.sabirdan.wework.service.membership;

import cz.cvut.fit.sabirdan.wework.domain.Membership;
import cz.cvut.fit.sabirdan.wework.domain.Project;
import cz.cvut.fit.sabirdan.wework.domain.User;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.DefaultMemberRole;
import cz.cvut.fit.sabirdan.wework.domain.role.member.MemberRole;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.Authorization;
import cz.cvut.fit.sabirdan.wework.domain.status.membership.MembershipStatus;
import cz.cvut.fit.sabirdan.wework.domain.status.project.ProjectStatus;
import cz.cvut.fit.sabirdan.wework.http.exception.BadRequestException;
import cz.cvut.fit.sabirdan.wework.http.exception.NotFoundException;
import cz.cvut.fit.sabirdan.wework.http.exception.UnauthorizedException;
import cz.cvut.fit.sabirdan.wework.http.request.ChangeMemberRoleRequest;
import cz.cvut.fit.sabirdan.wework.http.request.ChangeMembershipStatusRequest;
import cz.cvut.fit.sabirdan.wework.http.request.InviteRequest;
import cz.cvut.fit.sabirdan.wework.http.response.InviteResponse;
import cz.cvut.fit.sabirdan.wework.repository.MembershipRepository;
import cz.cvut.fit.sabirdan.wework.repository.ProjectRepository;
import cz.cvut.fit.sabirdan.wework.service.CrudServiceImpl;
import cz.cvut.fit.sabirdan.wework.service.role.member.MemberRoleService;
import cz.cvut.fit.sabirdan.wework.service.status.membership.MembershipStatusService;
import cz.cvut.fit.sabirdan.wework.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MembershipServiceImpl extends CrudServiceImpl<Membership> implements MembershipService {
    private final MembershipRepository membershipRepository;
    private final UserService userService;
    private final MemberRoleService memberRoleService;
    private final ProjectRepository projectRepository;
    private final MembershipStatusService membershipStatusService;

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

        if (!project.getStatus().getValue().equals(ProjectStatus.DEFAULT_STATUS_VALUE_OPEN))
            throw new BadRequestException("You cannot invite people to a closed project");

        MemberRole role = memberRoleService.findByName(inviteRequest.getRoleName());

        User user = userService.getByUsername(inviteRequest.getUsername());

        Membership membership = new Membership(
                user,
                project,
                inviter,
                role,
                membershipStatusService.getByValue(MembershipStatus.DEFAULT_STATUS_VALUE_PROPOSED)
        );

        if (inviter.isAuthorized(Authorization.SYSTEM_INVITE))
            return new InviteResponse(saveOrSubstituteMembership(membership).getId());

        Membership inviterMembership = findEnabledMembershipByProjectIdAndUsername(
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
    public Membership getById(Long id) {
        Membership membership = super.findById(id)
                .orElseThrow(() -> new NotFoundException("Membership does not exist"));
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (user.isAuthorized(Authorization.SYSTEM_READ_INVITATIONS))
            return membership;

        findEnabledMembershipByProjectIdAndUsername(membership.getProject().getId(), user.getUsername())
                .orElseThrow(() -> new UnauthorizedException("You are not a part of this project"));

        return membership;
    }

    @Override
    public Optional<Membership> findMembershipByProjectIdAndUsername(Long projectId, String username) {
        return membershipRepository.findMembershipByProjectIdAndUsername(projectId, username);
    }

    @Override
    public Optional<Membership> findEnabledMembershipByProjectIdAndUsername(Long projectId, String username) {
        return membershipRepository.findEnabledMembershipByProjectIdAndUsername(projectId, username);
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
        Optional<Membership> optionalEditorMembership = findEnabledMembershipByProjectIdAndUsername(membership.getProject().getId(), editor.getUsername());
        MembershipStatus membershipStatus = membershipStatusService.getByValue(changeMembershipStatusRequest.getStatusValue());

        final boolean hasMemberAuthority = optionalEditorMembership.map(membershipEditor ->
                        (membershipEditor.isAuthorized(Authorization.KICK) && membershipEditor.hasAuthorityOver(membership)))
                .orElse(false);
        final boolean hasSystemAuthority = editor.isAuthorized(Authorization.SYSTEM_CHANGE_MEMBERSHIP_STATUS) && editor.hasAuthorityOver(member);
        final boolean selfChange = editor.getUsername().equals(member.getUsername());

        if ((hasMemberAuthority || hasSystemAuthority)
                && (membership.getStatus().getValue().equals(MembershipStatus.DEFAULT_STATUS_VALUE_ENABLED) || membership.getStatus().getValue().equals(MembershipStatus.DEFAULT_STATUS_VALUE_PROPOSED))
                && membershipStatus.getValue().equals(MembershipStatus.DEFAULT_STATUS_VALUE_KICKED)) {

            List<Membership> memberships = projectRepository.getMembershipsByProjectId(membership.getProject().getId());
            if (memberships.size() == 1) {
                projectRepository.deleteById(membership.getProject().getId());
                return;
            }

            membership.kick(membershipStatus);

            MemberRole ownerRole = memberRoleService.findDefaultByName(DefaultMemberRole.OWNER.name());

            if (!membership.getRole().getId().equals(ownerRole.getId()))
                return;

            // grant owner's role to a random member so that project always has an owner
            memberships.stream()
                    .filter(m -> !Objects.equals(m.getId(), membership.getId()))
                    .findAny()
                    .ifPresent(m -> m.setRole(memberRoleService.findDefaultByName(DefaultMemberRole.OWNER.name())));

            return;
        }

        if (!selfChange && !hasSystemAuthority)
            throw new UnauthorizedException("You are not authorized to perform this action");

        if (membership.getStatus().getValue().equals(MembershipStatus.DEFAULT_STATUS_VALUE_PROPOSED) && membershipStatus.getValue().equals(MembershipStatus.DEFAULT_STATUS_VALUE_ENABLED)) {
            membership.accept(membershipStatus);
            return;
        }

        if (membership.getStatus().getValue().equals(MembershipStatus.DEFAULT_STATUS_VALUE_PROPOSED) && membershipStatus.getValue().equals(MembershipStatus.DEFAULT_STATUS_VALUE_REJECTED)) {
            membership.reject(membershipStatus);
            return;
        }

        if (membership.getStatus().getValue().equals(MembershipStatus.DEFAULT_STATUS_VALUE_ENABLED) && membershipStatus.getValue().equals(MembershipStatus.DEFAULT_STATUS_VALUE_LEFT)) {

            List<Membership> memberships = projectRepository.getMembershipsByProjectId(membership.getProject().getId());
            if (memberships.size() == 1) {
                projectRepository.deleteById(membership.getProject().getId());
                return;
            }

            membership.leave(membershipStatus);

            MemberRole ownerRole = memberRoleService.findDefaultByName(DefaultMemberRole.OWNER.name());

            if (!membership.getRole().getId().equals(ownerRole.getId()))
                return;

            // If it was a role of owner, then grant owner's role to a random member
            memberships.stream()
                    .filter(m -> !Objects.equals(m.getId(), membership.getId()))
                    .findAny()
                    .ifPresent(m -> m.setRole(ownerRole));

            return;
        }

        if (membership.getStatus().getValue().equals(MembershipStatus.DEFAULT_STATUS_VALUE_ENABLED) && membershipStatus.getValue().equals(MembershipStatus.DEFAULT_STATUS_VALUE_KICKED))
            throw new BadRequestException("You cannot kick yourself from the project");

        throw new BadRequestException("Invalid operation: cannot self change membership status from " + membership.getStatus().getName() + " to " + membershipStatus.getName());
    }

    @Override
    public List<Membership> getInvitations() {
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (user.isAuthorized(Authorization.SYSTEM_READ_INVITATIONS))
            return membershipRepository.findInvitations();

        return membershipRepository.findInvitationsByUsername(user.getUsername());
    }

    @Override
    public void changeMemberRole(Long membershipId, ChangeMemberRoleRequest changeMemberRoleRequest) {
        Membership membership = getById(membershipId);

        if (membership.getStatus().getValue().equals(MembershipStatus.DEFAULT_STATUS_VALUE_ENABLED) && membership.getStatus().getValue().equals(MembershipStatus.DEFAULT_STATUS_VALUE_PROPOSED))
            throw new BadRequestException("Cannot change a role to a disabled member");

        User editor = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = membership.getMember();

        MemberRole role = memberRoleService.findByName(changeMemberRoleRequest.getRoleName());

        Optional<Membership> optionalEditorMembership = findEnabledMembershipByProjectIdAndUsername(membership.getProject().getId(), editor.getUsername());

        final boolean selfChange = user.getUsername().equals(editor.getUsername());

        final boolean canChangeRole = optionalEditorMembership.map(editorMembership ->
                (editorMembership.isAuthorized(Authorization.CHANGE_ROLE))).orElse(false);

        final boolean hasMemberAuthority = optionalEditorMembership.map(editorMembership ->
                (editorMembership.hasAuthorityOver(membership))
        ).orElse(false);

        final boolean canChooseThisRole = optionalEditorMembership.map(editorMembership ->
                (editorMembership.hasAuthorityOver(role))
        ).orElse(false);

        final boolean hasSystemAuthority = editor.isAuthorized(Authorization.SYSTEM_CHANGE_ROLE) && editor.hasAuthorityOver(user);

        if (hasSystemAuthority) {
            membership.setRole(role);
            return;
        }

        if (selfChange)
            throw new BadRequestException("You cannot change yourself a role");

        if (!canChangeRole)
            throw new UnauthorizedException("You are not authorized to change roles");

        if (!hasMemberAuthority)
            throw new UnauthorizedException("You are not authorized to change role of this member");

        if (!canChooseThisRole)
            throw new UnauthorizedException("roleName", "You are not authorized to choose this role");

        membership.setRole(role);
    }

    @Override
    public Integer countEnabledMembershipsByProjectId(Long id) {
        return projectRepository.countEnabledMembershipsById(id);
    }
}
