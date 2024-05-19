package cz.cvut.fit.sabirdan.wework.domain.role;

import cz.cvut.fit.sabirdan.wework.domain.User;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.Authorization;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "system_roles")
public class SystemRole extends Role<User> {
    public SystemRole(String name, Set<Authorization> authorizations, int power) {
        super(name, authorizations, power);
    }
}
