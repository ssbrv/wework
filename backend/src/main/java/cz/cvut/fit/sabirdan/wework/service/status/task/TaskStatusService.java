package cz.cvut.fit.sabirdan.wework.service.status.task;

import cz.cvut.fit.sabirdan.wework.domain.status.task.TaskStatus;
import cz.cvut.fit.sabirdan.wework.service.status.StatusService;

public interface TaskStatusService extends StatusService<TaskStatus> {
    void initializeTaskStatuses();
}
