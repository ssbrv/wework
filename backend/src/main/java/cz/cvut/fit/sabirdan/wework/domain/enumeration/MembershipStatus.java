package cz.cvut.fit.sabirdan.wework.domain.enumeration;

public enum MembershipStatus {
    ENABLED, // default
    PROPOSED, // invited to the project
    REJECTED, // declined invitation
    KICKED, // kicked by other member who has authority
    LEFT // left the project
}
