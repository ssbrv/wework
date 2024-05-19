package cz.cvut.fit.sabirdan.wework.service.project;

import cz.cvut.fit.sabirdan.wework.domain.Membership;
import cz.cvut.fit.sabirdan.wework.domain.Project;
import cz.cvut.fit.sabirdan.wework.domain.User;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.Authorization;
import cz.cvut.fit.sabirdan.wework.http.exception.NotFoundException;
import cz.cvut.fit.sabirdan.wework.http.exception.UnauthorizedException;
import cz.cvut.fit.sabirdan.wework.http.request.CreateProjectRequest;
import cz.cvut.fit.sabirdan.wework.http.response.project.CreateProjectResponse;
import cz.cvut.fit.sabirdan.wework.repository.ProjectRepository;
import cz.cvut.fit.sabirdan.wework.service.CrudServiceImpl;
import cz.cvut.fit.sabirdan.wework.service.membership.MembershipService;
import cz.cvut.fit.sabirdan.wework.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl extends CrudServiceImpl<Project> implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final MembershipService membershipService;

    @Override
    public JpaRepository<Project, Long> getRepository() {
        return projectRepository;
    }

    @Override
    public String getEntityName() {
        return "Project";
    }

    @Override
    public CreateProjectResponse createProject(CreateProjectRequest createProjectRequest) {
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        // create and save the project
        Project project = new Project(createProjectRequest.getName(), createProjectRequest.getDescription());
        Project savedProject = projectRepository.save(project);

        // create and save membership
        Membership membership = new Membership(user, project);
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
}