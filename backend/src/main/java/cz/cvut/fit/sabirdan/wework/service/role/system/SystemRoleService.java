package cz.cvut.fit.sabirdan.wework.service.role.system;

import cz.cvut.fit.sabirdan.wework.domain.role.system.SystemRole;
import cz.cvut.fit.sabirdan.wework.service.CrudService;

public interface SystemRoleService extends CrudService<SystemRole> {
    SystemRole findDefaultByValue(String name);

    void initializeSystemRoles();

    SystemRole findByValue(String name);
}
