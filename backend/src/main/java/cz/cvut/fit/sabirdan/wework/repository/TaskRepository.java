package cz.cvut.fit.sabirdan.wework.repository;

import cz.cvut.fit.sabirdan.wework.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t JOIN t.author.memberships m where t.author.username = :username and m.status = cz.cvut.fit.sabirdan.wework.domain.enumeration.MembershipStatus.ENABLED and t.project.status = cz.cvut.fit.sabirdan.wework.domain.enumeration.ProjectStatus.ENABLED")
    List<Task> findAllByAuthorUsername(@Param("username") String username);

    @Query("SELECT t FROM Task t JOIN t.assignees u join u.memberships m WHERE u.username = :username and m.status = cz.cvut.fit.sabirdan.wework.domain.enumeration.MembershipStatus.ENABLED and t.project.status = cz.cvut.fit.sabirdan.wework.domain.enumeration.ProjectStatus.ENABLED")
    List<Task> findAllByAssigneeUsername(@Param("username") String username);

    List<Task> findAllByAssigneesNotEmpty();
}
