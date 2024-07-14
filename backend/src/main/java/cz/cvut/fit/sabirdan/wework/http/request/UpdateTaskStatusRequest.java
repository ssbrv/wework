package cz.cvut.fit.sabirdan.wework.http.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTaskStatusRequest {
    @NotBlank(message = "Please, make sure the task status is set properly")
    private String statusValue;
}
