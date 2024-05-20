package cz.cvut.fit.sabirdan.wework.controller;

import cz.cvut.fit.sabirdan.wework.http.request.CreateUpdateTaskRequest;
import cz.cvut.fit.sabirdan.wework.http.request.UpdateAssigneeRequest;
import cz.cvut.fit.sabirdan.wework.http.request.UpdateTaskStatusRequest;
import cz.cvut.fit.sabirdan.wework.http.response.task.CreateTaskResponse;
import cz.cvut.fit.sabirdan.wework.http.response.task.TaskDTO;
import cz.cvut.fit.sabirdan.wework.service.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping("authored")
    public ResponseEntity<Iterable<TaskDTO>> getAuthoredTasks() {
        return ResponseEntity.ok(taskService.findAllAuthoredTasks().stream().map(TaskDTO::new).collect(Collectors.toList()));
    }

    @GetMapping("assigned")
    public ResponseEntity<Iterable<TaskDTO>> getAssignedTasks() {
        return ResponseEntity.ok(taskService.findAllAssignedTasks().stream().map(TaskDTO::new).collect(Collectors.toList()));
    }

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<Iterable<TaskDTO>> getProjectTasks(@PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.findAllByProjectId(projectId).stream().map(TaskDTO::new).collect(Collectors.toList()));
    }

    @GetMapping("{taskId}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(new TaskDTO(taskService.getById(taskId)));
    }

    @PostMapping("/projects/{projectId}")
    public ResponseEntity<CreateTaskResponse> createProjectTask(@PathVariable Long projectId, @RequestBody @Validated CreateUpdateTaskRequest createTaskRequest) {
        return ResponseEntity.ok(taskService.createTask(projectId, createTaskRequest));
    }

    @PutMapping("{taskId}/assignees")
    public void updateAssignee(@PathVariable Long taskId, @RequestBody @Validated UpdateAssigneeRequest updateAssigneeRequest) {
        taskService.updateAssignee(taskId, updateAssigneeRequest);
    }

    @PutMapping("{taskId}")
    public void updateTask(@PathVariable Long taskId, @RequestBody @Validated CreateUpdateTaskRequest updateTaskRequest) {
        taskService.updateTask(taskId, updateTaskRequest);
    }

    @PutMapping("{taskId}/status")
    public void updateTaskStatus(@PathVariable Long taskId, @RequestBody @Validated UpdateTaskStatusRequest updateTaskStatusRequest) {
        taskService.updateTaskStatus(taskId, updateTaskStatusRequest);
    }

    @DeleteMapping("{taskId}")
    public void deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
    }
}
