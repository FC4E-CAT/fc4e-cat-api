package org.grnet.cat.enums;

public enum UserType {

    Identified(0),
    Validated(1),
    Admin(2);

    UserType(int severity) {
        this.severity = severity;
    }

    public int orderBy(UserType other) {
        return this.severity.compareTo(other.severity);
    }

    private Integer severity;
}
