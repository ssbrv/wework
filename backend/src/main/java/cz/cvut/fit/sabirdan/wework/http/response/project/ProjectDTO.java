package cz.cvut.fit.sabirdan.wework.http.response.project;

import cz.cvut.fit.sabirdan.wework.domain.Project;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private ProjectStatus status;
    private Integer memberCount;


    public ProjectDTO(Project project) {
        this(
                project,
                null
        );
    }

    public ProjectDTO(Project project, Integer memberCount) {
        this(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStatus(),
                memberCount
        );
    }
}
