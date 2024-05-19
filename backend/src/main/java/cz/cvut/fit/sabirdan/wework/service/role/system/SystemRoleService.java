package cz.cvut.fit.sabirdan.wework.service.role.system;

import cz.cvut.fit.sabirdan.wework.domain.role.SystemRole;
import cz.cvut.fit.sabirdan.wework.service.CrudService;
import org.springframework.beans.factory.annotation.Value;

public interface SystemRoleService extends CrudService<SystemRole> {
    SystemRole getSuperAdminSystemRole();
    SystemRole getUserSystemRole();

    void initializeSystemRoles();
}
