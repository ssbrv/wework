package cz.cvut.fit.sabirdan.wework.service.role;

import cz.cvut.fit.sabirdan.wework.domain.Role;
import cz.cvut.fit.sabirdan.wework.repository.RoleRepository;
import cz.cvut.fit.sabirdan.wework.service.CrudServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends CrudServiceImpl<Role> implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public JpaRepository<Role, Long> getRepository() {
        return roleRepository;
    }

    @Override
    public String getEntityName() {
        return "Role";
    }
}
