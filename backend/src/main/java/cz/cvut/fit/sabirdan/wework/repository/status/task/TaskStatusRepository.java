package cz.cvut.fit.sabirdan.wework.repository.status.task;

import cz.cvut.fit.sabirdan.wework.domain.status.task.TaskStatus;
import cz.cvut.fit.sabirdan.wework.repository.status.StatusRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskStatusRepository extends StatusRepository<TaskStatus> {
}
