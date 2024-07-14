package cz.cvut.fit.sabirdan.wework.service.status.project;

import cz.cvut.fit.sabirdan.wework.domain.status.project.ProjectStatus;
import cz.cvut.fit.sabirdan.wework.repository.status.project.ProjectStatusRepository;
import cz.cvut.fit.sabirdan.wework.service.status.StatusServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProjectStatusServiceImpl extends StatusServiceImpl<ProjectStatus, ProjectStatusRepository> implements ProjectStatusService {
    @Autowired
    public ProjectStatusServiceImpl(ProjectStatusRepository statusRepository) {
        super(statusRepository);
    }

    @Override
    public void initializeProjectStatuses() {
        if (!statusRepository.existsByValue(ProjectStatus.DEFAULT_STATUS_VALUE_OPEN))
            statusRepository.save(new ProjectStatus(ProjectStatus.DEFAULT_STATUS_VALUE_OPEN, ProjectStatus.DEFAULT_STATUS_NAME_OPEN));

        if (!statusRepository.existsByValue(ProjectStatus.DEFAULT_STATUS_VALUE_CLOSED))
            statusRepository.save(new ProjectStatus(ProjectStatus.DEFAULT_STATUS_VALUE_CLOSED, ProjectStatus.DEFAULT_STATUS_NAME_CLOSED));
    }

    @Override
    public JpaRepository<ProjectStatus, Long> getRepository() {
        return statusRepository;
    }

    @Override
    public String getEntityName() {
        return "Project status";
    }
}
