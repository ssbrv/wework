package cz.cvut.fit.sabirdan.wework.http.response.user;

import cz.cvut.fit.sabirdan.wework.domain.User;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.Sex;
import cz.cvut.fit.sabirdan.wework.http.response.role.RoleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SafeUserDTO {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private Sex sex;
    private RoleDTO role;

    public SafeUserDTO(User user) {
        this(
                String.valueOf(user.getId()),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getSex(),
                new RoleDTO(user.getRole())
        );
    }
}
