package cz.cvut.fit.sabirdan.wework.service.task;

import cz.cvut.fit.sabirdan.wework.domain.Task;
import cz.cvut.fit.sabirdan.wework.http.request.CreateUpdateTaskRequest;
import cz.cvut.fit.sabirdan.wework.http.request.UpdateAssigneeRequest;
import cz.cvut.fit.sabirdan.wework.http.request.UpdateTaskStatusRequest;
import cz.cvut.fit.sabirdan.wework.http.response.task.CreateTaskResponse;
import cz.cvut.fit.sabirdan.wework.service.CrudService;

import java.util.List;
import java.util.Set;

public interface TaskService extends CrudService<Task> {
    List<Task> findAllAuthoredTasks();

    List<Task> findAllAssignedTasks();

    Set<Task> findAllByProjectId(Long projectId);

    Task getById(Long taskId);

    CreateTaskResponse createTask(Long projectId, CreateUpdateTaskRequest createTaskRequest);

    void updateAssignee(Long taskId, UpdateAssigneeRequest updateAssigneeRequest);

    void updateTask(Long taskId, CreateUpdateTaskRequest updateTaskRequest);

    void updateTaskStatus(Long taskId, UpdateTaskStatusRequest updateTaskStatusRequest);

    void deleteTask(Long taskId);
}
