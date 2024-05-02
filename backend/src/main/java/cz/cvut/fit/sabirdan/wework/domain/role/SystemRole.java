package cz.cvut.fit.sabirdan.wework.domain.role;

import cz.cvut.fit.sabirdan.wework.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "system_roles")
public class SystemRole extends Role<User> {
}
