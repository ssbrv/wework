package cz.cvut.fit.sabirdan.wework.repository;

import cz.cvut.fit.sabirdan.wework.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
