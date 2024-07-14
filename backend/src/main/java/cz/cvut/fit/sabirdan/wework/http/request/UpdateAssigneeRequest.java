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
public class UpdateAssigneeRequest {
    @NotBlank(message = "This field is required")
    private String username;
    private boolean shouldBeAssigned;
}
