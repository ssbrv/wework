package cz.cvut.fit.sabirdan.wework.service.role.member;

import cz.cvut.fit.sabirdan.wework.domain.enumeration.Authorization;
import cz.cvut.fit.sabirdan.wework.domain.role.MemberRole;
import cz.cvut.fit.sabirdan.wework.repository.role.MemberRoleRepository;
import cz.cvut.fit.sabirdan.wework.service.CrudServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MemberRoleServiceImpl extends CrudServiceImpl<MemberRole> implements MemberRoleService {
    @Value("${application.member-role.owner.name}")
    private String ownerMemberRoleName;
    @Value("${application.member-role.member.name}")
    private String memberMemberRoleName;
    @Value("${application.member-role.admin.name}")
    private String adminMemberRoleName;

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

    @Override
    public MemberRole getOwnerMemberRole() {
        return memberRoleRepository.findByName(ownerMemberRoleName)
                .orElseThrow(() -> new RuntimeException("The member role was not initialized properly. Please, contact the tech support"));
    }

    @Override
    public MemberRole getAdminMemberRole() {
        return memberRoleRepository.findByName(adminMemberRoleName)
                .orElseThrow(() -> new RuntimeException("The member role was not initialized properly. Please, contact the tech support"));
    }

    @Override
    public MemberRole getMemberMemberRole() {
        return memberRoleRepository.findByName(memberMemberRoleName)
                .orElseThrow(() -> new RuntimeException("The member role was not initialized properly. Please, contact the tech support"));
    }

    @Override
    public void initializeMemberRoles() {
        List<MemberRole> initList = new ArrayList<>();
        MemberRole ownerMemberRole = new MemberRole(ownerMemberRoleName, Authorization.getAllMemberAuthorizations(), 90);
        MemberRole adminMemberRole = new MemberRole(adminMemberRoleName, Authorization.getAdminMemberRoleAuthorizations(), 60);
        MemberRole memberMemberRole = new MemberRole(memberMemberRoleName, Authorization.getMemberMemberRoleAuthorizations(), 10);

        if (!memberRoleRepository.existsByName(ownerMemberRoleName))
            initList.add(ownerMemberRole);

        if (!memberRoleRepository.existsByName(adminMemberRoleName))
            initList.add(adminMemberRole);

        if (!memberRoleRepository.existsByName(memberMemberRoleName))
            initList.add(memberMemberRole);

        memberRoleRepository.saveAll(initList);
    }

    @Override
    public Optional<MemberRole> findByName(String name) {
        return memberRoleRepository.findByName(name);
    }
}
