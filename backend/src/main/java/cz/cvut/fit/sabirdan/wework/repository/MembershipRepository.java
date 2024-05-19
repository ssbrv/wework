package cz.cvut.fit.sabirdan.wework.repository;

import cz.cvut.fit.sabirdan.wework.domain.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    @Query("SELECT m FROM Membership m WHERE m.project.id = :projectId and m.member.username = :username")
    Optional<Membership> findMembershipByProjectIdAndUsername(@Param("projectId") Long projectId, @Param("username") String username);
}
