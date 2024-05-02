package cz.cvut.fit.sabirdan.wework.service.role.system;

import cz.cvut.fit.sabirdan.wework.domain.role.SystemRole;
import cz.cvut.fit.sabirdan.wework.repository.role.SystemRoleRepository;
import cz.cvut.fit.sabirdan.wework.service.CrudServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class SystemRoleServiceImpl extends CrudServiceImpl<SystemRole> implements SystemRoleService {

    private final SystemRoleRepository systemRoleRepository;

    @Autowired
    public SystemRoleServiceImpl(SystemRoleRepository systemRoleRepository) {
        this.systemRoleRepository = systemRoleRepository;
    }

    @Override
    public JpaRepository<SystemRole, Long> getRepository() {
        return systemRoleRepository;
    }

    @Override
    public String getEntityName() {
        return "System Role";
    }
}
