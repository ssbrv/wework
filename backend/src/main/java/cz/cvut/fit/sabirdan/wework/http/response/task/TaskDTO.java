package cz.cvut.fit.sabirdan.wework.http.response.task;

import cz.cvut.fit.sabirdan.wework.domain.Task;
import cz.cvut.fit.sabirdan.wework.http.response.StatusDTO;
import cz.cvut.fit.sabirdan.wework.http.response.project.ProjectDTO;
import cz.cvut.fit.sabirdan.wework.http.response.user.SafeUserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDTO {
    private Long id;
    private StatusDTO status;
    private String summary;
    private String description;
    private ProjectDTO project;
    private SafeUserDTO author;
    private Set<SafeUserDTO> assignees;

    public TaskDTO(Task task) {
        this(
                task.getId(),
                new StatusDTO(task.getStatus()),
                task.getSummary(),
                task.getDescription(),
                new ProjectDTO(task.getProject()),
                (task.getAuthor() != null) ? new SafeUserDTO(task.getAuthor()) : null,
                new HashSet<>(task.getAssignees().stream().map(SafeUserDTO::new).collect(Collectors.toSet()))
        );
    }
}
