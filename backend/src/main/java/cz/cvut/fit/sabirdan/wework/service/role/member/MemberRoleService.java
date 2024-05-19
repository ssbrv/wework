package cz.cvut.fit.sabirdan.wework.service.role.member;

import cz.cvut.fit.sabirdan.wework.domain.role.MemberRole;
import cz.cvut.fit.sabirdan.wework.service.CrudService;

import java.util.Optional;

public interface MemberRoleService extends CrudService<MemberRole> {
    MemberRole getOwnerMemberRole();

    MemberRole getAdminMemberRole();

    MemberRole getMemberMemberRole();

    void initializeMemberRoles();

    Optional<MemberRole> findByName(String name);
}
