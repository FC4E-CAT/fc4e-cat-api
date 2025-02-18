package org.grnet.cat.dtos.assessment.registry;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({"timestamp", "code", "message"})
@Getter
@Setter
@AllArgsConstructor

public class TestExtraInfoNodeDto {

    public TestExtraInfoNodeDto() {
    }
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private String timestamp;

    private int code;

    private String message;


}
