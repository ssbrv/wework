package cz.cvut.fit.sabirdan.wework.service.task;

import cz.cvut.fit.sabirdan.wework.domain.Membership;
import cz.cvut.fit.sabirdan.wework.domain.Project;
import cz.cvut.fit.sabirdan.wework.domain.Task;
import cz.cvut.fit.sabirdan.wework.domain.User;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.Authorization;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.ProjectStatus;
import cz.cvut.fit.sabirdan.wework.domain.role.member.MemberRole;
import cz.cvut.fit.sabirdan.wework.domain.status.task.TaskStatus;
import cz.cvut.fit.sabirdan.wework.http.exception.BadRequestException;
import cz.cvut.fit.sabirdan.wework.http.exception.NotFoundException;
import cz.cvut.fit.sabirdan.wework.http.exception.UnauthorizedException;
import cz.cvut.fit.sabirdan.wework.http.request.CreateUpdateTaskRequest;
import cz.cvut.fit.sabirdan.wework.http.request.UpdateAssigneeRequest;
import cz.cvut.fit.sabirdan.wework.http.request.UpdateTaskStatusRequest;
import cz.cvut.fit.sabirdan.wework.http.response.task.CreateTaskResponse;
import cz.cvut.fit.sabirdan.wework.repository.TaskRepository;
import cz.cvut.fit.sabirdan.wework.service.CrudServiceImpl;
import cz.cvut.fit.sabirdan.wework.service.membership.MembershipService;
import cz.cvut.fit.sabirdan.wework.service.project.ProjectService;
import cz.cvut.fit.sabirdan.wework.service.status.task.TaskStatusService;
import cz.cvut.fit.sabirdan.wework.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl extends CrudServiceImpl<Task> implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ProjectService projectService;
    private final MembershipService membershipService;
    private final TaskStatusService taskStatusService;

    @Override
    public JpaRepository<Task, Long> getRepository() {
        return taskRepository;
    }

    @Override
    public String getEntityName() {
        return "Task";
    }

    @Override
    public List<Task> findAllAuthoredTasks() {
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (user.isAuthorized(Authorization.SYSTEM_READ_TASKS))
            return findAll(); // all tasks have an author

        return taskRepository.findAllByAuthorUsername(user.getUsername());
    }

    @Override
    public List<Task> findAllAssignedTasks() {
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (user.isAuthorized(Authorization.SYSTEM_READ_TASKS))
            return taskRepository.findAllByAssigneesNotEmpty(); // all tasks that have an assignee

        return taskRepository.findAllByAssigneeUsername(user.getUsername());
    }

    @Override
    public Set<Task> findAllByProjectId(Long projectId) {
        Project project = projectService.getProjectById(projectId);
        return project.getTasks();
    }

    @Override
    public Task getById(Long taskId) {
        Task task = findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task does not exist"));

        projectService.getProjectById(task.getProject().getId()); // this insures authorities
        return task;
    }

    @Override
    public CreateTaskResponse createTask(Long projectId, CreateUpdateTaskRequest createTaskRequest) {
        Project project = projectService.getProjectById(projectId);

        if (project.getStatus() != ProjectStatus.ENABLED)
            throw new BadRequestException("You cannot create new tasks in a closed project");

        User author = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        final boolean hasSystemAuthority = author.isAuthorized(Authorization.SYSTEM_CREATE_UPDATE_TASK);

        Optional<Membership> optionalMembership = membershipService.findEnabledMembershipByProjectIdAndUsername(projectId, author.getUsername());

        final boolean hasMemberAuthority = optionalMembership.map((membership -> membership.isAuthorized(Authorization.CREATE_UPDATE_TASK))).orElse(false);

        if (!hasMemberAuthority && !hasSystemAuthority)
            throw new UnauthorizedException("You are not authorized to create tasks in this project");

        Task task = new Task(createTaskRequest.getSummary(), createTaskRequest.getDescription(), project, author, taskStatusService.getByValue(TaskStatus.DEFAULT_STATUS_VALUE_TODO));
        return new CreateTaskResponse(save(task).getId());
    }



    @Override
    public void updateAssignee(Long taskId, UpdateAssigneeRequest updateAssigneeRequest) {
        Task task = findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task does not exist"));

        User editor = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Project project = projectService.getProjectById(task.getProject().getId()); // this ensures authorities

        if (project.getStatus() != ProjectStatus.ENABLED)
            throw new UnauthorizedException("You cannot update tasks in a closed project");

        final boolean hasSystemAuthority = editor.isAuthorized(Authorization.SYSTEM_CREATE_UPDATE_TASK);
        Optional<Membership> optionalMembership = membershipService.findEnabledMembershipByProjectIdAndUsername(project.getId(), editor.getUsername());

        final boolean hasMemberAuthority = optionalMembership.map((membership -> membership.isAuthorized(Authorization.CREATE_UPDATE_TASK))).orElse(false);

        if (!hasMemberAuthority && !hasSystemAuthority)
            throw new UnauthorizedException("You are not authorized to edit assignees in this project");

        MemberRole role = optionalMembership.map(Membership::getRole).orElse(null);

        User assignee = userService.findByUsername(updateAssigneeRequest.getUsername())
                .orElseThrow(() -> new NotFoundException("User does not exists with username \"" + updateAssigneeRequest.getUsername() + "\""));

        Membership assigneeMembership = membershipService.findEnabledMembershipByProjectIdAndUsername(task.getProject().getId(), assignee.getUsername())
                .orElseThrow(() -> new BadRequestException("User is not in the project with username \"" + assignee.getUsername() + "\""));

        if (assigneeMembership.hasAuthorityOver(role) && !hasSystemAuthority)
            throw new UnauthorizedException("You are not authorized to edit assignments of this user");

        if (updateAssigneeRequest.isShouldBeAssigned() && task.getAssignees().contains(assignee))
            throw new BadRequestException("This user is already assigned to this task");

        if (updateAssigneeRequest.isShouldBeAssigned()) {
            task.getAssignees().add(assignee);
            return;
        }

        if (!task.getAssignees().contains(assignee))
            throw new BadRequestException("username", "This user was not assigned to this task");

        task.getAssignees().remove(assignee);
    }

    @Override
    public void updateTask(Long taskId, CreateUpdateTaskRequest updateTaskRequest) {
        Task task = findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task does not exist"));

        User editor = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Project project = projectService.getProjectById(task.getProject().getId()); // this ensures authorities

        if (project.getStatus() != ProjectStatus.ENABLED)
            throw new UnauthorizedException("You cannot edit tasks in a closed project");

        final boolean hasSystemAuthority = editor.isAuthorized(Authorization.SYSTEM_CREATE_UPDATE_TASK);
        Optional<Membership> optionalMembership = membershipService.findEnabledMembershipByProjectIdAndUsername(project.getId(), editor.getUsername());

        final boolean hasMemberAuthority = optionalMembership.map((membership -> membership.isAuthorized(Authorization.CREATE_UPDATE_TASK))).orElse(false);

        if (!hasMemberAuthority && !hasSystemAuthority)
            throw new UnauthorizedException("You are not authorized to edit tasks in this project");

        task.setDescription(updateTaskRequest.getDescription());
        task.setSummary(updateTaskRequest.getSummary());
    }

    @Override
    public void updateTaskStatus(Long taskId, UpdateTaskStatusRequest updateTaskStatusRequest) {
        Task task = findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task does not exist"));

        User editor = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Project project = projectService.getProjectById(task.getProject().getId()); // this ensures authorities

        if (project.getStatus() != ProjectStatus.ENABLED)
            throw new UnauthorizedException("You cannot edit tasks in a closed project");

        final boolean hasSystemAuthority = editor.isAuthorized(Authorization.SYSTEM_CREATE_UPDATE_TASK);
        Optional<Membership> optionalMembership = membershipService.findEnabledMembershipByProjectIdAndUsername(project.getId(), editor.getUsername());

        final boolean hasMemberAuthority = optionalMembership.map((membership -> membership.isAuthorized(Authorization.CREATE_UPDATE_TASK))).orElse(false);

        if (!hasMemberAuthority && !hasSystemAuthority)
            throw new UnauthorizedException("You are not authorized to edit tasks in this project");

        task.setStatus(taskStatusService.getByValue(updateTaskStatusRequest.getStatusValue(), "statusValue"));
    }

    @Override
    public void deleteTask(Long taskId) {
        Task task = findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task does not exist"));

        User editor = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Project project = projectService.getProjectById(task.getProject().getId()); // this ensures authorities

        if (project.getStatus() != ProjectStatus.ENABLED)
            throw new UnauthorizedException("You cannot delete tasks in a closed project");

        final boolean hasSystemAuthority = editor.isAuthorized(Authorization.SYSTEM_DELETE_TASK);
        Optional<Membership> optionalMembership = membershipService.findEnabledMembershipByProjectIdAndUsername(project.getId(), editor.getUsername());

        final boolean hasMemberAuthority = optionalMembership.map((membership -> membership.isAuthorized(Authorization.DELETE_TASK))).orElse(false);

        if (!hasMemberAuthority && !hasSystemAuthority)
            throw new UnauthorizedException("You are not authorized to delete tasks in this project");

        deleteById(taskId);
    }
}
