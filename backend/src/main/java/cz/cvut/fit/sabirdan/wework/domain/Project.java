package cz.cvut.fit.sabirdan.wework.domain;

import cz.cvut.fit.sabirdan.wework.enumeration.ProjectStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "projects")
public class Project extends EntityWithIdLong {
    @Column(nullable = false)
    private String name;
    @Column
    private String description;
    @Column
    @Enumerated(EnumType.STRING)
    private ProjectStatus status = ProjectStatus.ENABLED;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> tasks = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Membership> memberships = new HashSet<>();

    // default project creation
    public Project(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
