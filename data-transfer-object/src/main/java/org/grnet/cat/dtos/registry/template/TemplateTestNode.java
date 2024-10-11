package org.grnet.cat.dtos.registry.template;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonPropertyOrder({ "id", "name", "description", "text", "param", "tooltip", "type", "value", "result", "evidence_url"})
@Getter
@Setter
public class TemplateTestNode extends TestNode{

    private Number value;

    private Number result;

    @JsonProperty("evidence_url")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> urls;

    public TemplateTestNode(String id, String name, String description, String type, List<String> urls, String text, String params, String toolTip) {
        super(id, name, description, type, text, params, toolTip);
        this.urls = urls;
    }
}
