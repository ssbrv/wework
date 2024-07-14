package cz.cvut.fit.sabirdan.wework.domain.status.project;

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
@Table(name = "project_statuses")
public class ProjectStatus extends Status {
    static public String DEFAULT_STATUS_VALUE_OPEN = "OPEN";
    static public String DEFAULT_STATUS_NAME_OPEN = "Open";

    static public String DEFAULT_STATUS_VALUE_CLOSED = "CLOSED";
    static public String DEFAULT_STATUS_NAME_CLOSED = "Closed";

    public ProjectStatus(String value, String name) {
        super(value, name);
    }
}
