package cz.cvut.fit.sabirdan.wework.domain.status.membership;

import cz.cvut.fit.sabirdan.wework.domain.status.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "membership_statuses")
public class MembershipStatus extends Status {
    static final public String DEFAULT_STATUS_VALUE_ENABLED = "ENABLED";
    static final public String DEFAULT_STATUS_NAME_ENABLED = "Enabled";

    static final public String DEFAULT_STATUS_VALUE_PROPOSED = "PROPOSED";
    static final public String DEFAULT_STATUS_NAME_PROPOSED = "Invited";

    static final public String DEFAULT_STATUS_VALUE_REJECTED = "REJECTED";
    static final public String DEFAULT_STATUS_NAME_REJECTED = "Rejected";

    static final public String DEFAULT_STATUS_VALUE_KICKED = "KICKED";
    static final public String DEFAULT_STATUS_NAME_KICKED = "Kicked";

    static final public String DEFAULT_STATUS_VALUE_LEFT = "LEFT";
    static final public String DEFAULT_STATUS_NAME_LEFT = "Left";

    public MembershipStatus(String value, String name) {
        super(value, name);
    }
}
