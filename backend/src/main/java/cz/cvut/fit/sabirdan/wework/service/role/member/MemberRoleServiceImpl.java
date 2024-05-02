package cz.cvut.fit.sabirdan.wework.service.role.member;

import cz.cvut.fit.sabirdan.wework.domain.role.MemberRole;
import cz.cvut.fit.sabirdan.wework.repository.role.MemberRoleRepository;
import cz.cvut.fit.sabirdan.wework.service.CrudServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberRoleServiceImpl extends CrudServiceImpl<MemberRole> implements MemberRoleService {

    private final MemberRoleRepository memberRoleRepository;

    @Autowired
    public MemberRoleServiceImpl(MemberRoleRepository memberRoleRepository) {
        this.memberRoleRepository = memberRoleRepository;
    }

    @Override
    public JpaRepository<MemberRole, Long> getRepository() {
        return memberRoleRepository;
    }

    @Override
    public String getEntityName() {
        return "Member Role";
    }
}
