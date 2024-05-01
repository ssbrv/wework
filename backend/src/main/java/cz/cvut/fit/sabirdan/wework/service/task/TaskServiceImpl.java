package cz.cvut.fit.sabirdan.wework.service.task;

import cz.cvut.fit.sabirdan.wework.domain.Task;
import cz.cvut.fit.sabirdan.wework.repository.TaskRepository;
import cz.cvut.fit.sabirdan.wework.service.CrudServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service

public class TaskServiceImpl extends CrudServiceImpl<Task> implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public JpaRepository<Task, Long> getRepository() {
        return taskRepository;
    }

    @Override
    public String getEntityName() {
        return "Task";
    }
}
