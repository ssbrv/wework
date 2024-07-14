package cz.cvut.fit.sabirdan.wework.http.response.role;

import cz.cvut.fit.sabirdan.wework.domain.role.Role;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.Authorization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDTO {
    private Long id;
    private String name;
    private int power;
    private Set<Authorization> authorizations;
    private String description;

    public RoleDTO(Role<?> role) {
        this(
                role.getId(),
                role.getValue(),
                role.getPower(),
                role.getAuthorizations(),
                role.getDescription()
        );
    }
}
