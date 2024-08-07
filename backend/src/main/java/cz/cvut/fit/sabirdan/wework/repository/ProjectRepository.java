package cz.cvut.fit.sabirdan.wework.repository;

import cz.cvut.fit.sabirdan.wework.domain.Membership;
import cz.cvut.fit.sabirdan.wework.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p JOIN p.memberships m WHERE m.member.username = :username and m.status.value = cz.cvut.fit.sabirdan.wework.domain.status.membership.MembershipStatus.DEFAULT_STATUS_VALUE_ENABLED")
    List<Project> findUserProjects(@Param("username") String username);

    @Query("SELECT count(m) FROM Project p JOIN p.memberships m WHERE p.id = :projectId AND m.status.value = cz.cvut.fit.sabirdan.wework.domain.status.membership.MembershipStatus.DEFAULT_STATUS_VALUE_ENABLED")
    Integer countEnabledMembershipsById(@Param("projectId") Long projectId);

    @Query("SELECT m FROM Project p JOIN p.memberships m WHERE p.id = :projectId and m.status.value = cz.cvut.fit.sabirdan.wework.domain.status.membership.MembershipStatus.DEFAULT_STATUS_VALUE_ENABLED")
    List<Membership> getMembershipsByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT m FROM Project p JOIN p.memberships m WHERE p.id = :projectId and m.status.value = cz.cvut.fit.sabirdan.wework.domain.status.membership.MembershipStatus.DEFAULT_STATUS_VALUE_PROPOSED")
    List<Membership> getInvitationsByProjectId(@Param("projectId") Long projectId);
}
