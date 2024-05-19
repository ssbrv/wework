package cz.cvut.fit.sabirdan.wework.domain;

import cz.cvut.fit.sabirdan.wework.domain.role.MemberRole;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.Authorization;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.MembershipStatus;
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
    @JoinColumn(name = "inviter_id")
    private User inviter = null;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    // TODO: assign default role
    @ManyToOne
    @JoinColumn(name = "role_id")
    private MemberRole role;

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
                      User inviter,
                      MemberRole role) {
        this.member = member;
        this.project = project;
        this.inviter = inviter;
        this.role = role;
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

    public void accept() {
        this.startedAt = LocalDateTime.now();
        this.status = MembershipStatus.ENABLED;
    }

    public void reject() {
        this.status = MembershipStatus.REJECTED;
    }

    public void leave() {
        this.endedAt = LocalDateTime.now();
        this.status = MembershipStatus.LEFT;
    }

    public void kick() {
        this.endedAt = LocalDateTime.now();
        this.status = MembershipStatus.KICKED;
    }
}
