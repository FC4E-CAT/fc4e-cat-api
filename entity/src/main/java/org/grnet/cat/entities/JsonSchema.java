package org.grnet.cat.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
/**
 * This entity represents the JsonSchema table in database.
 *
 */
@Entity
public class JsonSchema extends PanacheEntityBase {

    /**
     * As id
     */
    @Id
    private String id;

    @Column(name = "json_schema", columnDefinition = "json")
    private String jsonSchema;

    public static JsonSchema fetchById(String id){
        return findById(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJsonSchema() {
        return jsonSchema;
    }

    public void setJsonSchema(String jsonSchema) {
        this.jsonSchema = jsonSchema;
    }
}