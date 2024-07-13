package cz.cvut.fit.sabirdan.wework.service.status.task;

import cz.cvut.fit.sabirdan.wework.domain.status.task.TaskStatus;
import cz.cvut.fit.sabirdan.wework.repository.status.task.TaskStatusRepository;
import cz.cvut.fit.sabirdan.wework.service.status.StatusServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskStatusServiceImpl extends StatusServiceImpl<TaskStatus, TaskStatusRepository> implements TaskStatusService {
    @Autowired
    public TaskStatusServiceImpl(TaskStatusRepository statusRepository) {
        super(statusRepository);
    }

    @Override
    public void initializeTaskStatuses() {
        if (!statusRepository.existsByValue(TaskStatus.DEFAULT_STATUS_VALUE_TODO))
            statusRepository.save(new TaskStatus(TaskStatus.DEFAULT_STATUS_VALUE_TODO, TaskStatus.DEFAULT_STATUS_NAME_TODO));

        if (!statusRepository.existsByValue(TaskStatus.DEFAULT_STATUS_VALUE_IN_PROGRESS))
            statusRepository.save(new TaskStatus(TaskStatus.DEFAULT_STATUS_VALUE_IN_PROGRESS, TaskStatus.DEFAULT_STATUS_NAME_IN_PROGRESS));

        if (!statusRepository.existsByValue(TaskStatus.DEFAULT_STATUS_VALUE_COMPLETED))
            statusRepository.save(new TaskStatus(TaskStatus.DEFAULT_STATUS_VALUE_COMPLETED, TaskStatus.DEFAULT_STATUS_NAME_COMPLETED));
    }

    @Override
    public JpaRepository<TaskStatus, Long> getRepository() {
        return statusRepository;
    }

    @Override
    public String getEntityName() {
        return "Task status";
    }
}
