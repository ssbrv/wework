package cz.cvut.fit.sabirdan.wework.domain;

import cz.cvut.fit.sabirdan.wework.enumeration.Authorization;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role extends EntityWithIdLong {
    @Column(nullable = false)
    private String name;

    @ElementCollection
    @Column
    @Enumerated(EnumType.STRING)
    private Set<Authorization> authorizations = new HashSet<>();

    @Column
    private String description;

    @ManyToMany(mappedBy = "roles")
    private Set<Membership> memberships;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Authorization> getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(Set<Authorization> authorizations) {
        this.authorizations = authorizations;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Membership> getMemberships() {
        return memberships;
    }

    public void setMemberships(Set<Membership> memberships) {
        this.memberships = memberships;
    }

    public Boolean canRead() {
        return authorizations.contains(Authorization.READ);
    }

    public Boolean canWrite() {
        return  authorizations.contains(Authorization.WRITE);
    }
}
