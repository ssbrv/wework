package cz.cvut.fit.sabirdan.wework.domain.role.system;

import cz.cvut.fit.sabirdan.wework.domain.User;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.Authorization;
import cz.cvut.fit.sabirdan.wework.domain.role.Role;
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
    public static final String DEFAULT_ROLE_VALUE_SUPER_ADMIN = "SUPER_ADMIN";
    public static final String DEFAULT_ROLE_NAME_SUPER_ADMIN = "Super administrator";

    public static final String DEFAULT_ROLE_VALUE_USER = "USER";
    public static final String DEFAULT_ROLE_NAME_USER = "Basic user";

    public SystemRole(String value, String name, Set<Authorization> authorizations, int power) {
        super(value, name, authorizations, power);
    }
}
