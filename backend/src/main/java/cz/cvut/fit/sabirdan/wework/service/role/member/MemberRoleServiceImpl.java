package cz.cvut.fit.sabirdan.wework.service.role.member;

import cz.cvut.fit.sabirdan.wework.domain.enumeration.Authorization;
import cz.cvut.fit.sabirdan.wework.domain.enumeration.DefaultMemberRole;
import cz.cvut.fit.sabirdan.wework.domain.role.member.MemberRole;
import cz.cvut.fit.sabirdan.wework.domain.role.system.SystemRole;
import cz.cvut.fit.sabirdan.wework.http.exception.NotFoundException;
import cz.cvut.fit.sabirdan.wework.repository.role.MemberRoleRepository;
import cz.cvut.fit.sabirdan.wework.service.CrudServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MemberRoleServiceImpl extends CrudServiceImpl<MemberRole> implements MemberRoleService {
    private final MemberRoleRepository memberRoleRepository;

    @Autowired
    public MemberRoleServiceImpl(MemberRoleRepository memberRoleRepository) {
        this.memberRoleRepository = memberRoleRepository;
    }

    @Override
    public MemberRole findByName(String name) {
        return memberRoleRepository.findByName(name).orElseThrow(() -> new NotFoundException("roleName", "Role " + name + " does not exist"));
    }

    @Override
    public MemberRole findDefaultByName(String name) {
        return memberRoleRepository.findByName(name).orElseThrow(() -> new RuntimeException("Role " + name + " was not initialized. Contact tech support"));
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
        List<MemberRole> initList = new ArrayList<>();
        MemberRole ownerMemberRole = new MemberRole(DefaultMemberRole.OWNER.name(), Authorization.getAllMemberAuthorizations(), 90);
        MemberRole adminMemberRole = new MemberRole(DefaultMemberRole.ADMIN.name(), Authorization.getAdminMemberRoleAuthorizations(), 60);
        MemberRole memberMemberRole = new MemberRole(DefaultMemberRole.MEMBER.name(), Authorization.getMemberMemberRoleAuthorizations(), 10);

        if (!memberRoleRepository.existsByName(DefaultMemberRole.OWNER.name()))
            initList.add(ownerMemberRole);

        if (!memberRoleRepository.existsByName(DefaultMemberRole.ADMIN.name()))
            initList.add(adminMemberRole);

        if (!memberRoleRepository.existsByName(DefaultMemberRole.MEMBER.name()))
            initList.add(memberMemberRole);

        memberRoleRepository.saveAll(initList);
    }
}
