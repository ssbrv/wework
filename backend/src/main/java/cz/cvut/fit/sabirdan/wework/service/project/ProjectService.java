package cz.cvut.fit.sabirdan.wework.service.project;

import cz.cvut.fit.sabirdan.wework.domain.Membership;
import cz.cvut.fit.sabirdan.wework.domain.Project;
import cz.cvut.fit.sabirdan.wework.domain.User;
import cz.cvut.fit.sabirdan.wework.http.request.CreateUpdateProjectRequest;
import cz.cvut.fit.sabirdan.wework.http.response.project.CreateProjectResponse;
import cz.cvut.fit.sabirdan.wework.http.response.project.ProjectDTO;
import cz.cvut.fit.sabirdan.wework.http.response.user.SafeUserDTO;
import cz.cvut.fit.sabirdan.wework.service.CrudService;

import java.util.List;

public interface ProjectService extends CrudService<Project> {
    CreateProjectResponse createProject(CreateUpdateProjectRequest createProjectRequest);

    List<Project> findUserProjects(String username);

    Project getProjectById(Long projectId);

    Project getUserProjectById(Long projectId, String username);

    Iterable<ProjectDTO> getProjectsDTO();

    ProjectDTO getProjectDTOById(Long projectId);

    void updateProjectById(Long projectId, CreateUpdateProjectRequest updateProjectRequest);

    List<Membership> getMembershipsByProjectId(Long projectId);

    List<Membership> getInvitationsByProjectId(Long projectId);
}
