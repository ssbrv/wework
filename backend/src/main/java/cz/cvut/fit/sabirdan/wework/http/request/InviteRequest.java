package cz.cvut.fit.sabirdan.wework.http.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InviteRequest {
    private String username;
    private String roleName;
    private Long projectId;
}
