package cz.cvut.fit.sabirdan.wework.http.response.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectResponse {
    private Long id;
}
