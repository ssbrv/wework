package cz.cvut.fit.sabirdan.wework.controller;

import cz.cvut.fit.sabirdan.wework.http.response.StatusDTO;
import cz.cvut.fit.sabirdan.wework.service.status.membership.MembershipStatusService;
import cz.cvut.fit.sabirdan.wework.service.status.project.ProjectStatusService;
import cz.cvut.fit.sabirdan.wework.service.status.task.TaskStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/statuses")
@RequiredArgsConstructor
public class StatusController {
    private final TaskStatusService taskStatusService;
    private final ProjectStatusService projectStatusService;
    private final MembershipStatusService membershipStatusService;

    @GetMapping("task")
    public ResponseEntity<Iterable<StatusDTO>> getTaskStatuses() {
        return ResponseEntity.ok(taskStatusService.findAll().stream().map(StatusDTO::new).collect(Collectors.toList()));
    }

    @GetMapping("task/{id}")
    public ResponseEntity<StatusDTO> getTaskStatus(@PathVariable Long id) {
        return ResponseEntity.ok(new StatusDTO(taskStatusService.getById(id)));
    }

    @GetMapping("task/by-value/{value}")
    public ResponseEntity<StatusDTO> getTaskStatus(@PathVariable String value) {
        return ResponseEntity.ok(new StatusDTO(taskStatusService.getByValue(value)));
    }

    @GetMapping("project")
    public ResponseEntity<Iterable<StatusDTO>> getProjectStatuses() {
        return ResponseEntity.ok(projectStatusService.findAll().stream().map(StatusDTO::new).collect(Collectors.toList()));
    }

    @GetMapping("project/{id}")
    public ResponseEntity<StatusDTO> getProjectStatus(@PathVariable Long id) {
        return ResponseEntity.ok(new StatusDTO(projectStatusService.getById(id)));
    }

    @GetMapping("project/by-value/{value}")
    public ResponseEntity<StatusDTO> getProjectStatus(@PathVariable String value) {
        return ResponseEntity.ok(new StatusDTO(projectStatusService.getByValue(value)));
    }

    @GetMapping("membership")
    public ResponseEntity<Iterable<StatusDTO>> getMembershipStatuses() {
        return ResponseEntity.ok(membershipStatusService.findAll().stream().map(StatusDTO::new).collect(Collectors.toList()));
    }

    @GetMapping("membership/{id}")
    public ResponseEntity<StatusDTO> getMembershipStatus(@PathVariable Long id) {
        return ResponseEntity.ok(new StatusDTO(membershipStatusService.getById(id)));
    }

    @GetMapping("membership/by-value/{value}")
    public ResponseEntity<StatusDTO> getMembershipStatus(@PathVariable String value) {
        return ResponseEntity.ok(new StatusDTO(membershipStatusService.getByValue(value)));
    }
}