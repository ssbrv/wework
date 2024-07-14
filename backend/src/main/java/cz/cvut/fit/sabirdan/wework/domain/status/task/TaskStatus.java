package cz.cvut.fit.sabirdan.wework.domain.status.task;

import cz.cvut.fit.sabirdan.wework.domain.status.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "task_statuses")
public class TaskStatus extends Status {
    static final public String DEFAULT_STATUS_VALUE_TODO = "TO_DO";
    static final public String DEFAULT_STATUS_NAME_TODO = "To-do";

    static final public String DEFAULT_STATUS_VALUE_IN_PROGRESS = "IN_PROGRESS";
    static final public String DEFAULT_STATUS_NAME_IN_PROGRESS = "In progress";

    static final public String DEFAULT_STATUS_VALUE_COMPLETED = "COMPLETED";
    static final public String DEFAULT_STATUS_NAME_COMPLETED = "Completed";

    public TaskStatus(String value, String name) {
        super(value, name);
    }
}
