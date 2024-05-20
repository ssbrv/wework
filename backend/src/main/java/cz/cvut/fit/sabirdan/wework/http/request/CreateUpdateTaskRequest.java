package cz.cvut.fit.sabirdan.wework.http.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUpdateTaskRequest {
    @NotBlank(message = "Summary cannot be blank")
    @Size(max = 60, message = "Summary has to be maximum 60 characters long")
    private String summary;

    @Size(max = 1500, message = "Task description has to be maximum 1500 characters long")
    private String description;
}
