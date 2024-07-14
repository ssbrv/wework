package cz.cvut.fit.sabirdan.wework.http.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeMemberRoleRequest {
    @NotBlank(message = "Please, make sure a role was properly selected")
    String roleValue;
}
