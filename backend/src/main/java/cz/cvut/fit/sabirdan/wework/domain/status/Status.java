package cz.cvut.fit.sabirdan.wework.domain.status;

import cz.cvut.fit.sabirdan.wework.domain.EntityWithIdLong;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public class Status extends EntityWithIdLong {
    @Column(nullable = false, unique = true)
    protected String value;
    @Column(nullable = false)
    protected String name;
}
