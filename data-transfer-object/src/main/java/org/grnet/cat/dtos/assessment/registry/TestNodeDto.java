package org.grnet.cat.dtos.assessment.registry;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonPropertyOrder({ "id", "name", "description", "type", "text", "params", "value", "result", "tool_tip", "evidence_url","last_run"})
@Getter
@Setter
@AllArgsConstructor
public class TestNodeDto {

    public TestNodeDto() {
    }

    private String id;

    private String name;

    private String description;

    private String type;

    private String text;

    private String params;

    private Object value;

    private Number result;

    @JsonProperty("tool_tip")
    private String toolTip;

    @JsonProperty("evidence_url")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<EvidenceUrlDto> urls;

    @JsonProperty("last_run")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TestExtraInfoNodeDto lastRun;

}
