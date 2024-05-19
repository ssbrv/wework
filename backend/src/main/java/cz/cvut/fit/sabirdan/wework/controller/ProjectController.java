package cz.cvut.fit.sabirdan.wework.controller;

import cz.cvut.fit.sabirdan.wework.http.request.CreateProjectRequest;
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
    public ResponseEntity<CreateProjectResponse> createProject(@RequestBody @Validated CreateProjectRequest createProjectRequest) {
        return ResponseEntity.ok(projectService.createProject(createProjectRequest));
    }

    @GetMapping
    public ResponseEntity<Iterable<ProjectDTO>> getProjects() {
        return ResponseEntity.ok(projectService.findAll().stream().map(ProjectDTO::new).collect(Collectors.toList()));
    }

    @GetMapping("{projectId}")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(new ProjectDTO(projectService.getProjectById(projectId)));
    }

    // TODO: delete project
    // TODO: update project
    // TODO: read invites
    // TODO: read members
}
