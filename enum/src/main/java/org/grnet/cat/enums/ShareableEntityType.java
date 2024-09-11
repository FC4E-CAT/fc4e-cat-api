package org.grnet.cat.enums;

import lombok.Getter;

@Getter
public enum ShareableEntityType {

    ASSESSMENT("assessment");

    private String value;
    ShareableEntityType(String value) {
        this.value = value;
    }
}
