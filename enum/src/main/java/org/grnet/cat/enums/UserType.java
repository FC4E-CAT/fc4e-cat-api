package org.grnet.cat.enums;

import java.util.Arrays;
import java.util.Set;

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

    public static UserType retrieveByRole(String role) {

        switch (role) {
            case "validated":
                return UserType.Validated;
            case "admin":
                return UserType.Admin;
            default:
                return UserType.Identified;
        }
    }

    public static UserType mostSeverity(Set<UserType> userTypes){

        int severity = userTypes
                .stream()
                .mapToInt(at->at.severity)
                .max()
                .orElse(0);

        return bySeverity(severity);
    }

    private static UserType bySeverity(int severity) {

        return Arrays
                .stream(values())
                .filter(userType -> userType.severity == severity)
                .findFirst()
                .get();
    }
}
