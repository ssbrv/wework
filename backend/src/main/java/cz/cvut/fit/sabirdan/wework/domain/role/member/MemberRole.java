package cz.cvut.fit.sabirdan.wework.domain.role.member;

import cz.cvut.fit.sabirdan.wework.domain.Membership;
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
@Table(name = "member_roles")
public class MemberRole extends Role<Membership> {
    public static final String DEFAULT_ROLE_VALUE_OWNER = "OWNER";
    public static final String DEFAULT_ROLE_NAME_OWNER = "Owner";

    public static final String DEFAULT_ROLE_VALUE_ADMIN = "ADMIN";
    public static final String DEFAULT_ROLE_NAME_ADMIN = "Administrator";

    public static final String DEFAULT_ROLE_VALUE_MEMBER = "MEMBER";
    public static final String DEFAULT_ROLE_NAME_MEMBER = "Basic member";

    public MemberRole(String value, String name, Set<Authorization> authorizations, int power) {
        super(value, name, authorizations, power);
    }
}
