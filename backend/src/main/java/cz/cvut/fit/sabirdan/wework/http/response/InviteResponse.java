package cz.cvut.fit.sabirdan.wework.http.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InviteResponse {
    private Long invitationId;
}
