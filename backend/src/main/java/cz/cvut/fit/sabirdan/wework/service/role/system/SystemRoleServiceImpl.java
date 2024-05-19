package cz.cvut.fit.sabirdan.wework.service.role.system;

import cz.cvut.fit.sabirdan.wework.domain.enumeration.Authorization;
import cz.cvut.fit.sabirdan.wework.domain.role.SystemRole;
import cz.cvut.fit.sabirdan.wework.repository.role.SystemRoleRepository;
import cz.cvut.fit.sabirdan.wework.service.CrudServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SystemRoleServiceImpl extends CrudServiceImpl<SystemRole> implements SystemRoleService {
    @Value("${application.system-role.super-admin.name}")
    private String superAdminSystemRoleName;
    @Value("${application.system-role.user.name}")
    private String userSystemRoleName;
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

    @Override
    public SystemRole getSuperAdminSystemRole() {
        return systemRoleRepository.findByName(superAdminSystemRoleName)
                .orElseThrow(() -> new RuntimeException("The system role was not initialized properly. Please, contact the tech support"));
    }

    @Override
    public SystemRole getUserSystemRole() {
        return systemRoleRepository.findByName(userSystemRoleName)
                .orElseThrow(() -> new RuntimeException("The system role was not initialized properly. Please, contact the tech support"));
    }

    @Override
    public void initializeSystemRoles() {
        List<SystemRole> initList = new ArrayList<>();
        SystemRole superAdminSystemRole = new SystemRole(superAdminSystemRoleName, Authorization.getAllSystemAuthorizations(), 90);
        SystemRole userSystemRole = new SystemRole(userSystemRoleName, Authorization.getUserSystemRoleAuthorizations(), 10);

        if (!systemRoleRepository.existsByName(superAdminSystemRoleName))
            initList.add(superAdminSystemRole);

        if (!systemRoleRepository.existsByName(userSystemRoleName))
            initList.add(userSystemRole);

        systemRoleRepository.saveAll(initList);
    }
}
