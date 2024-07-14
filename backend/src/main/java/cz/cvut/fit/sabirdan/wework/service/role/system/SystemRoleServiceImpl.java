package cz.cvut.fit.sabirdan.wework.service.role.system;

import cz.cvut.fit.sabirdan.wework.domain.enumeration.Authorization;
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
    public SystemRole findByValue(String value) {
        return systemRoleRepository.findByValue(value).orElseThrow(() -> new NotFoundException("roleValue", "Role with " + value + " does not exist"));
    }

    @Override
    public SystemRole findDefaultByValue(String value) {
        return systemRoleRepository.findByValue(value).orElseThrow(() -> new RuntimeException("Role with value " + value + " was not initialized. Contact tech support"));
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
        if (!systemRoleRepository.existsByValue(SystemRole.DEFAULT_ROLE_VALUE_SUPER_ADMIN))
            systemRoleRepository.save(new SystemRole(SystemRole.DEFAULT_ROLE_VALUE_SUPER_ADMIN, SystemRole.DEFAULT_ROLE_NAME_SUPER_ADMIN, Authorization.getAllSystemAuthorizations(), 90));

        if (!systemRoleRepository.existsByValue(SystemRole.DEFAULT_ROLE_VALUE_USER))
            systemRoleRepository.save(new SystemRole(SystemRole.DEFAULT_ROLE_VALUE_USER, SystemRole.DEFAULT_ROLE_NAME_USER, Authorization.getUserSystemRoleAuthorizations(), 10));
    }
}
