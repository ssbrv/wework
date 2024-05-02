package cz.cvut.fit.sabirdan.wework.repository.role;

import cz.cvut.fit.sabirdan.wework.domain.role.SystemRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemRoleRepository extends JpaRepository<SystemRole, Long> {
}
