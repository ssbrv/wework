package cz.cvut.fit.sabirdan.wework.domain;

import cz.cvut.fit.sabirdan.wework.domain.role.MemberRole;
import cz.cvut.fit.sabirdan.wework.enumeration.MembershipStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "memberships")
public class Membership extends EntityWithIdLong {
    @Column
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column
    private LocalDateTime startedAt;
    @Column
    private LocalDateTime endedAt;
    @Column
    @Enumerated(EnumType.STRING)
    private MembershipStatus status = MembershipStatus.PROPOSED;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    // TODO: assign default role
    @ManyToOne
    @JoinColumn(name = "member_role_id")
    private MemberRole memberRole;

    // owner
    public Membership(User member,
                      Project project) {
        this.startedAt = LocalDateTime.now();
        this.status = MembershipStatus.ENABLED;
        this.member = member;
        this.project = project;
        // TODO: set owner role
    }

    // invitation
    public Membership(User member,
                      Project project,
                      MemberRole memberRole) {
        this.member = member;
        this.project = project;
        this.memberRole = memberRole;
    }
}
