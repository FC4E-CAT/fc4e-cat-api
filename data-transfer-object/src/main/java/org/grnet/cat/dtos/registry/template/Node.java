package org.grnet.cat.dtos.registry.template;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {

    @JsonIgnore
    private List<Node> children = new ArrayList<>();

    public List<Node> getChildren() {
        return children;
    }

    public void addChild(Node child) {
        this.children.add(child);
    }
}
