package cz.cvut.fit.sabirdan.wework.domain;

import jakarta.persistence.*;

@MappedSuperclass
public class EntityWithIdLong {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    protected Long id;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}