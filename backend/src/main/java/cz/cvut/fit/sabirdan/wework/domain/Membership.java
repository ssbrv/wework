package cz.cvut.fit.sabirdan.wework.domain;

import cz.cvut.fit.sabirdan.wework.enumeration.MembershipStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class Membership extends EntityWithIdLong {
    @Column
    private LocalDateTime sendInvitationDateTime;
    @Column
    private LocalDateTime acceptInvitationDateTime;
    @Column
    private LocalDateTime startMembershipDateTime;
    @Column
    private LocalDateTime endMembershipDateTime;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MembershipStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToMany
    @JoinTable(name = "roles",
            joinColumns = @JoinColumn(name = "membership_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public LocalDateTime getSendInvitationDateTime() {
        return sendInvitationDateTime;
    }

    public void setSendInvitationDateTime(LocalDateTime sendInvitationDateTime) {
        this.sendInvitationDateTime = sendInvitationDateTime;
    }

    public LocalDateTime getAcceptInvitationDateTime() {
        return acceptInvitationDateTime;
    }

    public void setAcceptInvitationDateTime(LocalDateTime acceptInvitationDateTime) {
        this.acceptInvitationDateTime = acceptInvitationDateTime;
    }

    public LocalDateTime getStartMembershipDateTime() {
        return startMembershipDateTime;
    }

    public void setStartMembershipDateTime(LocalDateTime startMembershipDateTime) {
        this.startMembershipDateTime = startMembershipDateTime;
    }

    public LocalDateTime getEndMembershipDateTime() {
        return endMembershipDateTime;
    }

    public void setEndMembershipDateTime(LocalDateTime endMembershipDateTime) {
        this.endMembershipDateTime = endMembershipDateTime;
    }

    public MembershipStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
