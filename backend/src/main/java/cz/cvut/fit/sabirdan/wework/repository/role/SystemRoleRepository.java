package cz.cvut.fit.sabirdan.wework.repository.role;

import cz.cvut.fit.sabirdan.wework.domain.role.system.SystemRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemRoleRepository extends JpaRepository<SystemRole, Long> {
    Optional<SystemRole> findByName(String name);
    boolean existsByName(String name);
}
