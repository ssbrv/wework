package cz.cvut.fit.sabirdan.wework.service.role.member;

import cz.cvut.fit.sabirdan.wework.domain.enumeration.Authorization;
import cz.cvut.fit.sabirdan.wework.domain.role.member.MemberRole;
import cz.cvut.fit.sabirdan.wework.http.exception.NotFoundException;
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
    public MemberRole findByValue(String value) {
        return memberRoleRepository.findByValue(value).orElseThrow(() -> new NotFoundException("roleValue", "Role with value " + value + " does not exist"));
    }

    @Override
    public MemberRole findDefaultByValue(String value) {
        return memberRoleRepository.findByValue(value).orElseThrow(() -> new RuntimeException("Role with value " + value + " was not initialized. Contact tech support"));
    }

    @Override
    public JpaRepository<MemberRole, Long> getRepository() {
        return memberRoleRepository;
    }

    @Override
    public String getEntityName() {
        return "Member Role";
    }

    @Override
    public void initializeMemberRoles() {
        if (!memberRoleRepository.existsByValue(MemberRole.DEFAULT_ROLE_VALUE_OWNER))
            memberRoleRepository.save(new MemberRole(MemberRole.DEFAULT_ROLE_VALUE_OWNER, MemberRole.DEFAULT_ROLE_NAME_OWNER, Authorization.getAllMemberAuthorizations(), 90));

        if (!memberRoleRepository.existsByValue(MemberRole.DEFAULT_ROLE_VALUE_ADMIN))
            memberRoleRepository.save(new MemberRole(MemberRole.DEFAULT_ROLE_VALUE_ADMIN, MemberRole.DEFAULT_ROLE_NAME_ADMIN, Authorization.getAdminMemberRoleAuthorizations(), 60));

        if (!memberRoleRepository.existsByValue(MemberRole.DEFAULT_ROLE_VALUE_MEMBER))
            memberRoleRepository.save(new MemberRole(MemberRole.DEFAULT_ROLE_VALUE_MEMBER, MemberRole.DEFAULT_ROLE_NAME_MEMBER, Authorization.getMemberMemberRoleAuthorizations(), 10));
    }
}
