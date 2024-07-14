package cz.cvut.fit.sabirdan.wework.service.role.member;

import cz.cvut.fit.sabirdan.wework.domain.role.member.MemberRole;
import cz.cvut.fit.sabirdan.wework.service.CrudService;

public interface MemberRoleService extends CrudService<MemberRole> {
    void initializeMemberRoles();

    MemberRole findByValue(String name);
    MemberRole findDefaultByValue(String name);
}
