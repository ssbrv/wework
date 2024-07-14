package cz.cvut.fit.sabirdan.wework.repository.status.project;

import cz.cvut.fit.sabirdan.wework.domain.status.project.ProjectStatus;
import cz.cvut.fit.sabirdan.wework.repository.status.StatusRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectStatusRepository extends StatusRepository<ProjectStatus> {
}
