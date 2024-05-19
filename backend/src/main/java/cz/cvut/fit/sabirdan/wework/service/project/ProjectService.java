package cz.cvut.fit.sabirdan.wework.service.project;

import cz.cvut.fit.sabirdan.wework.domain.Project;
import cz.cvut.fit.sabirdan.wework.http.request.CreateProjectRequest;
import cz.cvut.fit.sabirdan.wework.http.response.project.CreateProjectResponse;
import cz.cvut.fit.sabirdan.wework.service.CrudService;

import java.util.List;

public interface ProjectService extends CrudService<Project> {
    CreateProjectResponse createProject(CreateProjectRequest createProjectRequest);

    List<Project> findUserProjects(String username);

    Project getProjectById(Long projectId);

    Project getUserProjectById(Long projectId, String username);
}
