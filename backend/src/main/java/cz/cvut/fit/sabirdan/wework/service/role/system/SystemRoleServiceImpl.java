package cz.cvut.fit.sabirdan.wework.service.role.system;

import cz.cvut.fit.sabirdan.wework.domain.enumeration.Authorization;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.DefaultSystemRole;
import cz.cvut.fit.sabirdan.wework.domain.role.system.SystemRole;
import cz.cvut.fit.sabirdan.wework.http.exception.NotFoundException;
import cz.cvut.fit.sabirdan.wework.repository.role.SystemRoleRepository;
import cz.cvut.fit.sabirdan.wework.service.CrudServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SystemRoleServiceImpl extends CrudServiceImpl<SystemRole> implements SystemRoleService {
    private final SystemRoleRepository systemRoleRepository;

    @Autowired
    public SystemRoleServiceImpl(SystemRoleRepository systemRoleRepository) {
        this.systemRoleRepository = systemRoleRepository;
    }

    @Override
    public SystemRole findByName(String name) {
        return systemRoleRepository.findByName(name).orElseThrow(() -> new NotFoundException("roleName", "Role " + name + " does not exist"));
    }

    @Override
    public SystemRole findDefaultByName(String name) {
        return systemRoleRepository.findByName(name).orElseThrow(() -> new RuntimeException("Role " + name + " was not initialized. Contact tech support"));
    }

    @Override
    public JpaRepository<SystemRole, Long> getRepository() {
        return systemRoleRepository;
    }

    @Override
    public String getEntityName() {
        return "System Role";
    }

    @Override
    public void initializeSystemRoles() {
        if (!systemRoleRepository.existsByName(DefaultSystemRole.SUPER_ADMIN.name()))
            systemRoleRepository.save(new SystemRole(DefaultSystemRole.SUPER_ADMIN.name(), Authorization.getAllSystemAuthorizations(), 90));

        if (!systemRoleRepository.existsByName(DefaultSystemRole.USER.name()))
            systemRoleRepository.save(new SystemRole(DefaultSystemRole.USER.name(), Authorization.getUserSystemRoleAuthorizations(), 10));
    }
}
