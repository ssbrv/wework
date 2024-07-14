package cz.cvut.fit.sabirdan.wework.repository.status.membership;

import cz.cvut.fit.sabirdan.wework.domain.status.membership.MembershipStatus;
import cz.cvut.fit.sabirdan.wework.repository.status.StatusRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipStatusRepository extends StatusRepository<MembershipStatus> {
}
