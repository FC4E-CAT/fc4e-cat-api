package org.grnet.cat.validators;

import org.grnet.cat.enums.ValidationStatus;

public class ValidationRequestValidator {

    /**
     * Validates the rejection reason based on the given validation status.
     *
     * @param status The status of the validation request.
     * @param rejectionReason The reason for rejection.
     * @throws IllegalArgumentException if the rejection reason is invalid when the status is REJECTED or is provided when status is not REJECTED.
     */
    public static void validateRejectionReason(ValidationStatus status, String rejectionReason) {
        if (status == ValidationStatus.REJECTED) {
            if (rejectionReason == null || rejectionReason.trim().isEmpty()) {
                throw new IllegalArgumentException("Rejection reason must not be empty when status is REJECTED.");
            }
        } else if (rejectionReason != null && !rejectionReason.trim().isEmpty()) {
            throw new IllegalArgumentException("Rejection reason should not be provided for statuses other than REJECTED.");
        }
    }
}
