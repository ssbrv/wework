package cz.cvut.fit.sabirdan.wework.domain;

import cz.cvut.fit.sabirdan.wework.domain.role.SystemRole;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.Authorization;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.Sex;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends EntityWithIdLong implements UserDetails {
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Sex sex = Sex.UNSPECIFIED;

    @Column(nullable = false)
    private LocalDateTime lastFullLogoutDate = LocalDateTime.now().minusSeconds(1);

    @ManyToOne
    @JoinColumn(name = "role_id")
    private SystemRole role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Membership> memberships = new HashSet<>();

    @OneToMany(mappedBy = "inviter")
    private Set<Membership> sentInvitations = new HashSet<>();

    @OneToMany(mappedBy = "author")
    private Set<Task> authoredTasks = new HashSet<>();

    @ManyToMany(mappedBy = "assignees")
    private Set<Task> assignedTasks = new HashSet<>();

    // status of the user
    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false)
    private boolean locked = false;

    // default user registration
    public User(String username,
                String password,
                String firstName,
                String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        if (role == null)
            return authorities;

        for (Authorization authorization : role.getAuthorizations())
            authorities.add(new SimpleGrantedAuthority(authorization.name()));

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public void setLastFullLogoutDate(LocalDateTime lastFullLogoutDate) {
        this.lastFullLogoutDate = lastFullLogoutDate.minusSeconds(1);
    }

    public boolean isAuthorized(Authorization authorization) {
        return (this.role != null && this.role.isAuthorized(authorization));
    }

    public boolean hasAuthorityOver(User user) {
        return (this.role != null && this.role.hasAuthorityOver(user.getRole()));
    }

    public boolean hasAuthorityOver(SystemRole role) {
        return (this.role != null && this.role.hasAuthorityOver(role));
    }

    @PreRemove
    private void preRemove() {
        for (Task task : authoredTasks)
            task.setAuthor(null);

        for (Task task : assignedTasks)
            task.getAssignees().remove(this);

        for (Membership invitation : sentInvitations)
            invitation.setInviter(null);
    }
}
