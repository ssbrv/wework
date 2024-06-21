package cz.cvut.fit.sabirdan.wework.service.role.member;

import cz.cvut.fit.sabirdan.wework.domain.role.member.MemberRole;
import cz.cvut.fit.sabirdan.wework.service.CrudService;

import java.util.Optional;

public interface MemberRoleService extends CrudService<MemberRole> {
    void initializeMemberRoles();

    MemberRole findByName(String name);
    MemberRole findDefaultByName(String name);
}
