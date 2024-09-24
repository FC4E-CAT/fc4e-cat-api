package org.grnet.cat.validators;

import jakarta.ws.rs.BadRequestException;
import java.util.List;

public class SortAndOrderValidator {

    public static void validateSortAndOrder(String sort, String order, List<String> validSortValues, List<String> validOrderValues) {

        if (!validOrderValues.contains(order)) {
            throw new BadRequestException("Invalid order value. Available values are: " + validOrderValues);
        }
        if (!validSortValues.contains(sort)) {
            throw new BadRequestException("Invalid sort value. Available values are: " + validSortValues);
        }
    }
}
