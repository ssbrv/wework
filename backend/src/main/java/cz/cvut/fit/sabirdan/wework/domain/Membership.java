package cz.cvut.fit.sabirdan.wework.domain;

import cz.cvut.fit.sabirdan.wework.domain.role.member.MemberRole;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.Authorization;
import cz.cvut.fit.sabirdan.wework.domain.status.membership.MembershipStatus;
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

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private MembershipStatus status;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    @ManyToOne
    @JoinColumn(name = "inviter_id")
    private User inviter = null;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private MemberRole role;

    // owner
    public Membership(User member,
                      Project project,
                      MemberRole role,
                      MembershipStatus statusEnabled) {
        this.startedAt = LocalDateTime.now();
        this.status = statusEnabled;
        this.member = member;
        this.project = project;
        this.role = role;
    }

    // invitation
    public Membership(User member,
                      Project project,
                      User inviter,
                      MemberRole role,
                      MembershipStatus statusProposed) {
        this.member = member;
        this.project = project;
        this.inviter = inviter;
        this.role = role;
        this.status = statusProposed;
    }

    public boolean isAuthorized(Authorization authorization) {
        return (this.role != null && this.role.isAuthorized(authorization));
    }

    public boolean hasAuthorityOver(Membership membership) {
        return (this.role != null && this.role.hasAuthorityOver(membership.getRole()));
    }

    public boolean hasAuthorityOver(MemberRole role) {
        return (this.role != null && this.role.hasAuthorityOver(role));
    }

    public void accept(MembershipStatus statusEnabled) {
        this.startedAt = LocalDateTime.now();
        this.status = statusEnabled;
    }

    public void reject(MembershipStatus statusRejected) {
        this.status = statusRejected;
    }

    public void leave(MembershipStatus statusLeft) {
        this.endedAt = LocalDateTime.now();
        this.status = statusLeft;
    }

    public void kick(MembershipStatus statusKicked) {
        this.endedAt = LocalDateTime.now();
        this.status = statusKicked;
    }
}
