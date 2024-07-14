package cz.cvut.fit.sabirdan.wework.http.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InviteRequest {
    @NotBlank(message = "This field is required")
    private String username;
    @NotBlank(message = "This field is required")
    private String roleValue;
    private Long projectId;
}
