package cz.cvut.fit.sabirdan.wework.http.response.membership;

import cz.cvut.fit.sabirdan.wework.domain.Membership;
import cz.cvut.fit.sabirdan.wework.http.response.StatusDTO;
import cz.cvut.fit.sabirdan.wework.http.response.project.ProjectDTO;
import cz.cvut.fit.sabirdan.wework.http.response.role.RoleDTO;
import cz.cvut.fit.sabirdan.wework.http.response.user.SafeUserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MembershipDTO {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private StatusDTO status;
    private SafeUserDTO member;
    private SafeUserDTO inviter;
    private ProjectDTO project;
    private RoleDTO role;

    public MembershipDTO(Membership membership) {
        this(membership, null);
    }

    public MembershipDTO(Membership membership, Integer memberCounter) {
        this(
                membership.getId(),
                membership.getCreatedAt(),
                membership.getStartedAt(),
                membership.getEndedAt(),
                new StatusDTO(membership.getStatus()),
                new SafeUserDTO(membership.getMember()),
                (membership.getInviter() != null) ? new SafeUserDTO(membership.getInviter()) : null,
                new ProjectDTO(membership.getProject(), memberCounter),
                new RoleDTO(membership.getRole())
        );
    }
}
