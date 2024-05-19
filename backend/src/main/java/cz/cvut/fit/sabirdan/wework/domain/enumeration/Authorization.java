package cz.cvut.fit.sabirdan.wework.domain.enumeration;

import java.util.Set;

public enum Authorization {
    // TODO
    SYSTEM_EDIT_USERS, SYSTEM_READ_PROJECTS, SYSTEM_INVITE, SYSTEM_EDIT_PROJECT_BASIC, SYSTEM_CHANGE_MEMBERSHIP_STATUS,
    INVITE, KICK, EDIT_PROJECT_BASIC, DELETE_PROJECT;

    public static Set<Authorization> getAllSystemAuthorizations() {
        return Set.of(SYSTEM_EDIT_USERS, SYSTEM_READ_PROJECTS, SYSTEM_INVITE, SYSTEM_EDIT_PROJECT_BASIC, SYSTEM_CHANGE_MEMBERSHIP_STATUS);
    }

    public static Set<Authorization> getUserSystemRoleAuthorizations() {
        return Set.of();
    }

    public static Set<Authorization> getAllMemberAuthorizations() {
        return Set.of(INVITE, KICK, EDIT_PROJECT_BASIC, DELETE_PROJECT);
    }

    public static Set<Authorization> getMemberMemberRoleAuthorizations() {
        return Set.of(EDIT_PROJECT_BASIC);
    }

    public static Set<Authorization> getAdminMemberRoleAuthorizations() {
        return Set.of(INVITE, KICK, EDIT_PROJECT_BASIC);
    }
}
