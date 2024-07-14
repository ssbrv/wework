package cz.cvut.fit.sabirdan.wework.service.status.membership;

import cz.cvut.fit.sabirdan.wework.domain.status.membership.MembershipStatus;
import cz.cvut.fit.sabirdan.wework.repository.status.membership.MembershipStatusRepository;
import cz.cvut.fit.sabirdan.wework.service.status.StatusServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MembershipStatusServiceImpl extends StatusServiceImpl<MembershipStatus, MembershipStatusRepository> implements MembershipStatusService {
    @Autowired
    public MembershipStatusServiceImpl(MembershipStatusRepository statusRepository) {
        super(statusRepository);
    }

    @Override
    public void initializeMembershipStatuses() {
        if (!statusRepository.existsByValue(MembershipStatus.DEFAULT_STATUS_VALUE_ENABLED))
            statusRepository.save(new MembershipStatus(MembershipStatus.DEFAULT_STATUS_VALUE_ENABLED, MembershipStatus.DEFAULT_STATUS_NAME_ENABLED));

        if (!statusRepository.existsByValue(MembershipStatus.DEFAULT_STATUS_VALUE_PROPOSED))
            statusRepository.save(new MembershipStatus(MembershipStatus.DEFAULT_STATUS_VALUE_PROPOSED, MembershipStatus.DEFAULT_STATUS_NAME_PROPOSED));

        if (!statusRepository.existsByValue(MembershipStatus.DEFAULT_STATUS_VALUE_REJECTED))
            statusRepository.save(new MembershipStatus(MembershipStatus.DEFAULT_STATUS_VALUE_REJECTED, MembershipStatus.DEFAULT_STATUS_NAME_REJECTED));

        if (!statusRepository.existsByValue(MembershipStatus.DEFAULT_STATUS_VALUE_KICKED))
            statusRepository.save(new MembershipStatus(MembershipStatus.DEFAULT_STATUS_VALUE_KICKED, MembershipStatus.DEFAULT_STATUS_NAME_KICKED));

        if (!statusRepository.existsByValue(MembershipStatus.DEFAULT_STATUS_VALUE_LEFT))
            statusRepository.save(new MembershipStatus(MembershipStatus.DEFAULT_STATUS_VALUE_LEFT, MembershipStatus.DEFAULT_STATUS_NAME_LEFT));
    }

    @Override
    public JpaRepository<MembershipStatus, Long> getRepository() {
        return statusRepository;
    }

    @Override
    public String getEntityName() {
        return "Project status";
    }
}
