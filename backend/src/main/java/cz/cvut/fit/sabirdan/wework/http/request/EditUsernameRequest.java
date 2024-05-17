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
public class EditUsernameRequest {
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 5, message = "Username has to be at least 5 characters long")
    @Size(max = 15, message = "Username has to be maximum 15 characters long")
    @Pattern(regexp = "^\\S+$", message = "Username cannot contain white spaces")
    private String username;
}
