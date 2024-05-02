package cz.cvut.fit.sabirdan.wework.repository.role;

import cz.cvut.fit.sabirdan.wework.domain.role.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {
}
