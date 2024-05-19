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
public class CreateUpdateProjectRequest {
    @NotBlank(message = "Project name cannot be blank")
    @Size(max = 60, message = "Project name has to be maximum 60 characters long")
    private String name;
    @Size(max = 1500, message = "Project description has to be maximum 1500 characters long")
    private String description;
}
