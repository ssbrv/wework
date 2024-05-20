package cz.cvut.fit.sabirdan.wework.http.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAssigneeRequest {
    private String username;
    private boolean shouldBeAssigned;
}
