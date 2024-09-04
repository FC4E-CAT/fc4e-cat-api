package org.grnet.cat.entities;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class AssessmentPerActor{

    public Long total;
    public String actor_name;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getActor_name() {
        return actor_name;
    }

    public void setActor_name(String actor_name) {
        this.actor_name = actor_name;
    }
}
