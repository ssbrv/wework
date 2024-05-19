package cz.cvut.fit.sabirdan.wework.repository;

import cz.cvut.fit.sabirdan.wework.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p JOIN p.memberships m WHERE m.member.username = :username and m.status = cz.cvut.fit.sabirdan.wework.domain.enumeration.MembershipStatus.ENABLED")
    List<Project> findUserProjects(@Param("username") String username);
}
