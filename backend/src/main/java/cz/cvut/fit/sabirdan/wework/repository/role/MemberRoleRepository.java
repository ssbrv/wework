package cz.cvut.fit.sabirdan.wework.repository.role;

import cz.cvut.fit.sabirdan.wework.domain.role.member.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {
    boolean existsByName(String name);
    Optional<MemberRole> findByName(String name);
}
