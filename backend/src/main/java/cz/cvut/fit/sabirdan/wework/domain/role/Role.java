package cz.cvut.fit.sabirdan.wework.domain.role;

import cz.cvut.fit.sabirdan.wework.domain.EntityWithIdLong;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.Authorization;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class Role<CLASS_HOLDER> extends EntityWithIdLong {
    @Column(nullable = false, unique = true)
    protected String name;

    @Column(nullable = false)
    protected int power;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column
    @Enumerated(EnumType.STRING)
    protected Set<Authorization> authorizations = new HashSet<>();

    @Column
    protected String description;

    @OneToMany(mappedBy = "role", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    protected Set<CLASS_HOLDER> roleHolders = new HashSet<>();

    // default role creation
    public Role(String name, Set<Authorization> authorizations, int power) {
        this.name = name;
        this.authorizations = authorizations;
        this.power = power;
    }

    public boolean isAuthorized(Authorization authorization) { return authorizations.contains(authorization); }

    public boolean hasAuthorityOver(Role<?> role) {
        return role == null || power > role.power;
    }
}
