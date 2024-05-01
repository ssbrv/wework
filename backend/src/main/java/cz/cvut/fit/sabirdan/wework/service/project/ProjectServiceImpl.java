package cz.cvut.fit.sabirdan.wework.service.project;

import cz.cvut.fit.sabirdan.wework.domain.Project;
import cz.cvut.fit.sabirdan.wework.repository.ProjectRepository;
import cz.cvut.fit.sabirdan.wework.service.CrudServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl extends CrudServiceImpl<Project> implements ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }


    @Override
    public JpaRepository<Project, Long> getRepository() {
        return projectRepository;
    }

    @Override
    public String getEntityName() {
        return "Project";
    }
}
