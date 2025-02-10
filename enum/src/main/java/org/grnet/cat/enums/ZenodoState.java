package org.grnet.cat.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ZenodoState {
    PROCESS_INIT("PROCESS INIT"),
    DEPOSIT_CREATED("DEPOSIT CREATED"),
    FILE_UPLOADED_TO_DEPOSIT("FILE UPLOADED TO DEPOSIT"),
    DEPOSIT_PUBLISHED("DEPOSIT PUBLISHED"),
    PROCESS_COMPLETED("PROCESS COMPLETED INIT"),
    PROCESS_FAILED("PROCESS FAILED");

    private String type;

    ZenodoState(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static List<String> getTypes() {
        return Arrays.stream(ZenodoState.values())
                .map(ZenodoState::getType)
                .collect(Collectors.toList());
    }
}
