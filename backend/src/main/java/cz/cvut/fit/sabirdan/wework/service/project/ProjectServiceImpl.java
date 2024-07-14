package cz.cvut.fit.sabirdan.wework.service.project;

import cz.cvut.fit.sabirdan.wework.domain.Membership;
import cz.cvut.fit.sabirdan.wework.domain.Project;
import cz.cvut.fit.sabirdan.wework.domain.User;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.Authorization;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.DefaultMemberRole;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.MembershipStatus;
import cz.cvut.fit.sabirdan.wework.domain.status.project.ProjectStatus;
import cz.cvut.fit.sabirdan.wework.http.exception.BadRequestException;
import cz.cvut.fit.sabirdan.wework.http.exception.NotFoundException;
import cz.cvut.fit.sabirdan.wework.http.exception.UnauthorizedException;
import cz.cvut.fit.sabirdan.wework.http.request.ChangeMembershipStatusRequest;
import cz.cvut.fit.sabirdan.wework.http.request.ChangeProjectStatusRequest;
import cz.cvut.fit.sabirdan.wework.http.request.CreateUpdateProjectRequest;
import cz.cvut.fit.sabirdan.wework.http.response.project.CreateProjectResponse;
import cz.cvut.fit.sabirdan.wework.http.response.project.ProjectDTO;
import cz.cvut.fit.sabirdan.wework.repository.ProjectRepository;
import cz.cvut.fit.sabirdan.wework.service.CrudServiceImpl;
import cz.cvut.fit.sabirdan.wework.service.membership.MembershipService;
import cz.cvut.fit.sabirdan.wework.service.role.member.MemberRoleService;
import cz.cvut.fit.sabirdan.wework.service.status.project.ProjectStatusService;
import cz.cvut.fit.sabirdan.wework.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl extends CrudServiceImpl<Project> implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final MembershipService membershipService;
    private final MemberRoleService memberRoleService;
    private final ProjectStatusService projectStatusService;

    @Override
    public JpaRepository<Project, Long> getRepository() {
        return projectRepository;
    }

    @Override
    public String getEntityName() {
        return "Project";
    }

    @Override
    public CreateProjectResponse createProject(CreateUpdateProjectRequest createProjectRequest) {
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        Project project = new Project(createProjectRequest.getName(), createProjectRequest.getDescription());
        Project savedProject = projectRepository.save(project);

        Membership membership = new Membership(user, project, memberRoleService.findDefaultByName(DefaultMemberRole.OWNER.name()));
        savedProject.getMemberships().add(membership);

        return CreateProjectResponse.builder().id(savedProject.getId()).build();
    }

    @Override
    public List<Project> findUserProjects(String username) {
        return projectRepository.findUserProjects(username);
    }

    @Override
    public List<Project> findAll() {
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        // return all projects if user has authority
        if (user.isAuthorized(Authorization.SYSTEM_READ_PROJECTS))
            return super.findAll();

        // otherwise return only user's projects
        return findUserProjects(user.getUsername());
    }

    @Override
    public Project getProjectById(Long projectId) {
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        // return project if user has authority
        if (user.isAuthorized(Authorization.SYSTEM_READ_PROJECTS))
            return findById(projectId).orElseThrow(() -> new NotFoundException("Project does not exist"));

        // otherwise return only if its user's project
        return getUserProjectById(projectId, user.getUsername());
    }

    @Override
    public Project getUserProjectById(Long projectId, String username) {
        if (!projectRepository.existsById(projectId))
            throw new NotFoundException("Project does not exist");

        Membership membership = membershipService.findMembershipByProjectIdAndUsername(projectId, username).orElseThrow(
                () -> new UnauthorizedException("You are not a part of this project")
        );

        switch (membership.getStatus()) {
            case ENABLED -> {
                return membership.getProject();
            }
            case LEFT -> throw new UnauthorizedException("You left this project");
            case KICKED -> throw new UnauthorizedException("You were kicked from this project");
            case PROPOSED -> throw new UnauthorizedException("You are not a part of this project yet. Check your invitations");
            default -> throw new RuntimeException("Unknown membership status. Please, contact tech support");
        }
    }

    @Override
    public Iterable<ProjectDTO> getProjectsDTO() {
        return findAll().stream().map((project) -> new ProjectDTO(project, projectRepository.countEnabledMembershipsById(project.getId()))).collect(Collectors.toList());
    }

    @Override
    public ProjectDTO getProjectDTOById(Long projectId) {
        return new ProjectDTO(getProjectById(projectId), projectRepository.countEnabledMembershipsById(projectId));
    }

    @Override
    public void updateProjectById(Long projectId, CreateUpdateProjectRequest updateProjectRequest) {
        Project project = getProjectById(projectId);
        if (!project.getStatus().getValue().equals(ProjectStatus.DEFAULT_STATUS_VALUE_OPEN))
            throw new BadRequestException("You cannot edit closed project's name and description");

        User editor = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Optional<Membership> optionalMembership = membershipService.findEnabledMembershipByProjectIdAndUsername(projectId, editor.getUsername());

        final boolean hasMemberAuthority = (optionalMembership.map(membership -> (membership.isAuthorized(Authorization.EDIT_PROJECT_BASIC))).orElse(false));
        final boolean hasSystemAuthority = (editor.isAuthorized(Authorization.SYSTEM_EDIT_PROJECT_BASIC));

        if (!hasMemberAuthority && !hasSystemAuthority)
            throw new UnauthorizedException("You are not authorized to edit project's name and description");

        project.setName(updateProjectRequest.getName());
        project.setDescription(updateProjectRequest.getDescription());
    }

    @Override
    public List<Membership> getMembershipsByProjectId(Long projectId) {
        getProjectById(projectId); // this ensures authorizations
        return projectRepository.getMembershipsByProjectId(projectId);
    }

    @Override
    public List<Membership> getInvitationsByProjectId(Long projectId) {
        getProjectById(projectId); // this ensures authorizations
        return projectRepository.getInvitationsByProjectId(projectId);
    }

    @Override
    public void leaveProject(Long projectId) {
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        getUserProjectById(projectId, user.getUsername()); // this ensures authorizations and project existence

        Membership membership = membershipService.findEnabledMembershipByProjectIdAndUsername(projectId, user.getUsername())
                .orElseThrow(() -> new BadRequestException("You are not a part of this project"));

        membershipService.changeMembershipStatus(membership.getId(), new ChangeMembershipStatusRequest(MembershipStatus.LEFT));
    }

    @Override
    public void changeProjectStatus(Long projectId, ChangeProjectStatusRequest changeProjectStatusRequest) {
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Project project = getProjectById(projectId); // this ensures authorizations
        ProjectStatus status = projectStatusService.getByValue(changeProjectStatusRequest.getStatusValue(), "statusValue");

        if (user.isAuthorized(Authorization.SYSTEM_CHANGE_PROJECT_STATUS)) {
            project.setStatus(status);
            return;
        }

        Membership membership = membershipService.findEnabledMembershipByProjectIdAndUsername(projectId, user.getUsername())
                .orElseThrow(() -> new UnauthorizedException("You are not a part of this project"));

        if (!membership.isAuthorized(Authorization.CHANGE_PROJECT_STATUS))
            throw new UnauthorizedException("You are not authorized to change project status");

        project.setStatus(status);
    }

    @Override
    public void deleteProject(Long projectId) {
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        getProjectById(projectId); // this ensures authorizations

        if (user.isAuthorized(Authorization.SYSTEM_DELETE_PROJECT)) {
            deleteById(projectId);
            return;
        }

        Membership membership = membershipService.findEnabledMembershipByProjectIdAndUsername(projectId, user.getUsername())
                .orElseThrow(() -> new UnauthorizedException("You are not a part of this project"));

        if (!membership.isAuthorized(Authorization.DELETE_PROJECT))
            throw new UnauthorizedException("You are not authorized to delete this project");

        deleteById(projectId);
    }
}