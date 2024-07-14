package cz.cvut.fit.sabirdan.wework.repository.role;

import cz.cvut.fit.sabirdan.wework.domain.role.member.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {
    boolean existsByValue(String value);
    Optional<MemberRole> findByValue(String value);
}
