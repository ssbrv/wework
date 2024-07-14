package cz.cvut.fit.sabirdan.wework.service.status.project;

import cz.cvut.fit.sabirdan.wework.domain.status.project.ProjectStatus;
import cz.cvut.fit.sabirdan.wework.service.status.StatusService;

public interface ProjectStatusService extends StatusService<ProjectStatus> {
    void initializeProjectStatuses();
}
