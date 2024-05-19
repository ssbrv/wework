package cz.cvut.fit.sabirdan.wework.http.request;

import cz.cvut.fit.sabirdan.wework.domain.enumeration.Sex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 5, message = "Username has to be at least 5 characters long")
    @Size(max = 15, message = "Username has to be maximum 15 characters long")
    @Pattern(regexp = "^\\S+$", message = "Username cannot contain white spaces")
    private String username;

    @NotBlank(message = "First Name cannot be blank")
    @Size(max = 20, message = "First Name has to be maximum 20 characters long")
    @Pattern(regexp = "^\\S.*", message = "First Name must not start with a white space")
    @Pattern(regexp = ".*\\S$", message = "First Name must not end with a white space")
    private String firstName;

    @NotBlank(message = "Last Name cannot be blank")
    @Size(max = 20, message = "Last Name has to be maximum 20 characters long")
    @Pattern(regexp = "^\\S.*", message = "Last Name must not start with a white space")
    @Pattern(regexp = ".*\\S$", message = "Last Name must not end with a white space")
    private String lastName;

    private Sex sex;
}
