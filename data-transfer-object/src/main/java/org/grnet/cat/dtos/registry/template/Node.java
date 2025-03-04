package org.grnet.cat.dtos.registry.template;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.TreeSet;

public abstract class Node implements Comparable<Node> {

    @Getter
    @Setter
    private String id;

    public Node(String id){
        this.id  = id;
    }

    @JsonIgnore
    private Set<Node> children = new TreeSet<>();

    public Set<Node> getChildren() {
        return children;
    }

    public void addChild(Node child) {
        this.children.add(child);
    }

    @Override
    public int compareTo(Node other) {
        return this.id.compareTo(other.id);
    }
}
