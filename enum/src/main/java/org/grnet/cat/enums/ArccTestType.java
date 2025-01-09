package org.grnet.cat.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ArccTestType {
    MD1a("MD-1a"),
    MD1b1("MD-1b1"),
    MD1b2("MD-1b2");
  //  MD1c("MD-1c");
    private String type;

    ArccTestType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
    public static List<String> getTypes() {
        return Arrays.stream(ArccTestType.values())
                .map(ArccTestType::getType)
                .collect(Collectors.toList());
    }

}
