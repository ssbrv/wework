package cz.cvut.fit.sabirdan.wework.domain.enumeration;

import java.util.Set;

public enum Authorization {
    // TODO
    SYSTEM_EDIT_USERS, SYSTEM_READ_PROJECTS, SYSTEM_INVITE,
    INVITE, SYSTEM_CHANGE_MEMBERSHIP_STATUS, KICK;

    public static Set<Authorization> getAllAuthorizations() {
        return Set.of(SYSTEM_EDIT_USERS);
    }

    public static Set<Authorization> getUserAuthorizations() {
        return Set.of();
    }
}
