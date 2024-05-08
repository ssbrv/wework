package cz.cvut.fit.sabirdan.wework.http.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 5, message = "Username has to be at least 5 characters long")
    @Size(max = 15, message = "Username has to be maximum 15 characters long")
    @Pattern(regexp = "^\\S+$", message = "Username cannot contain white spaces")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password has to be at least 8 characters long")
    @Size(max = 60, message = "Password has to be maximum 60 characters long")
    @Pattern(regexp = "^\\S+$", message = "Password cannot contain white spaces")
    private String password;

    @NotBlank(message = "First Name cannot be blank")
    @Size(max = 20, message = "First Name has to be maximum 20 characters long")
    @Pattern(regexp = "^\\S.*\\S$", message = "First Name must not start or end with white spaces")
    private String firstName;

    @NotBlank(message = "Last Name cannot be blank")
    @Size(max = 20, message = "Last Name has to be maximum 20 characters long")
    @Pattern(regexp = "^\\S.*\\S$", message = "Last Name must not start or end with white spaces")
    private String lastName;
}
