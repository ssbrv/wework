package cz.cvut.fit.sabirdan.wework.domain.role;

import cz.cvut.fit.sabirdan.wework.domain.Membership;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "member_roles")
public class MemberRole extends Role<Membership> {
}
