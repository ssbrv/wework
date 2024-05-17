package cz.cvut.fit.sabirdan.wework.http.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password has to be at least 8 characters long")
    @Size(max = 60, message = "Password has to be maximum 60 characters long")
    @Pattern(regexp = "^\\S+$", message = "Password cannot contain white spaces")
    private String newPassword;
}
