package cz.cvut.fit.sabirdan.wework.domain.role;

import cz.cvut.fit.sabirdan.wework.domain.EntityWithIdLong;
import cz.cvut.fit.sabirdan.wework.enumeration.Authorization;
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
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int power;

    @ElementCollection
    @Column
    @Enumerated(EnumType.STRING)
    private Set<Authorization> authorizations = new HashSet<>();

    @Column
    private String description;

    @OneToMany(mappedBy = "role")
    private Set<CLASS_HOLDER> roleHolders = new HashSet<>();

    // default role creation
    public Role(String name, Set<Authorization> authorizations, String description) {
        this.name = name;
        this.authorizations = authorizations;
        this.description = description;
    }

    boolean authorized(Authorization authorization) { return authorizations.contains(authorization); }
}
