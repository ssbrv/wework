package cz.cvut.fit.sabirdan.wework.enumeration;

public enum MembershipStatus {
    ENABLED, // default
    DISABLED, // disabled by system admin
    PROPOSED, // invited to the project
    REJECTED, // declined invitation
    KICKED, // kicked by other member who has authority
    LEFT // left the project
}
