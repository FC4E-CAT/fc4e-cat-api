package org.grnet.cat.enums;

public enum ValidationStatus {

    PENDING(0),
    REVIEW(1),
    APPROVED(2),
    REJECTED(3);

    ValidationStatus(int severity) {
        this.severity = severity;
    }

    public int orderBy(ValidationStatus other) {
        return this.severity.compareTo(other.severity);
    }

    private Integer severity;
}
