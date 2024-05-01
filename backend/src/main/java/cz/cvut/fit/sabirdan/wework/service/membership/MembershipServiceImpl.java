package cz.cvut.fit.sabirdan.wework.service.membership;

import cz.cvut.fit.sabirdan.wework.domain.Membership;
import cz.cvut.fit.sabirdan.wework.repository.MembershipRepository;
import cz.cvut.fit.sabirdan.wework.service.CrudServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class MembershipServiceImpl extends CrudServiceImpl<Membership> implements MembershipService {
    private final MembershipRepository membershipRepository;

    @Autowired
    public MembershipServiceImpl(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    @Override
    public JpaRepository<Membership, Long> getRepository() {
        return membershipRepository;
    }

    @Override
    public String getEntityName() {
        return "Membership";
    }
}
