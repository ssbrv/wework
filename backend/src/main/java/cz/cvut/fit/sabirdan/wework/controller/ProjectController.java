package cz.cvut.fit.sabirdan.wework.controller;

import cz.cvut.fit.sabirdan.wework.http.request.ChangeProjectStatusRequest;
import cz.cvut.fit.sabirdan.wework.http.request.CreateUpdateProjectRequest;
import cz.cvut.fit.sabirdan.wework.http.response.membership.MembershipDTO;
import cz.cvut.fit.sabirdan.wework.http.response.project.CreateProjectResponse;
import cz.cvut.fit.sabirdan.wework.http.response.project.ProjectDTO;
import cz.cvut.fit.sabirdan.wework.service.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<CreateProjectResponse> createProject(@RequestBody @Validated CreateUpdateProjectRequest createProjectRequest) {
        return ResponseEntity.ok(projectService.createProject(createProjectRequest));
    }

    @PutMapping("{projectId}")
    public void updateProject(@PathVariable Long projectId, @RequestBody @Validated CreateUpdateProjectRequest updateProjectRequest) {
        projectService.updateProjectById(projectId, updateProjectRequest);
    }

    @GetMapping
    public ResponseEntity<Iterable<ProjectDTO>> getProjects() {
        return ResponseEntity.ok(projectService.getProjectsDTO());
    }

    @GetMapping("{projectId}")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getProjectDTOById(projectId));
    }

    @GetMapping("{projectId}/memberships")
    public ResponseEntity<Iterable<MembershipDTO>> getMemberships(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getMembershipsByProjectId(projectId).stream().map(MembershipDTO::new).collect(Collectors.toList()));
    }

    @GetMapping("{projectId}/invitations")
    public ResponseEntity<Iterable<MembershipDTO>> getInvitations(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getInvitationsByProjectId(projectId).stream().map(MembershipDTO::new).collect(Collectors.toList()));
    }

    @PutMapping("{projectId}/membership")
    public void leaveProject(@PathVariable Long projectId) {
        projectService.leaveProject(projectId);
    }

    @PutMapping("{projectId}/status")
    public void changeProjectStatus(@PathVariable Long projectId, @Validated @RequestBody ChangeProjectStatusRequest changeProjectStatusRequest) {
        projectService.changeProjectStatus(projectId, changeProjectStatusRequest);
    }

    @DeleteMapping("{projectId}")
    public void deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
    }

}
