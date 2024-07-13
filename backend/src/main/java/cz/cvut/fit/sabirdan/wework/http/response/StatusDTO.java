package cz.cvut.fit.sabirdan.wework.http.response;

import cz.cvut.fit.sabirdan.wework.domain.status.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusDTO {
    private Long id;
    private String name;
    private String value;

    public StatusDTO(Status status) {
        this(
                status.getId(),
                status.getName(),
                status.getValue()
        );
    }
}
