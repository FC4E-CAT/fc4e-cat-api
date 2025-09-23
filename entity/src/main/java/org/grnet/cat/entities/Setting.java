package org.grnet.cat.entities;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.sql.Timestamp;
import java.util.Map;

@Entity
@Table(name = "t_Setting")
@Getter
@Setter
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Type(JsonType.class)
    @Column(name = "setting_data", columnDefinition = "jsonb")
    public Map<String, Object> data;

    @Column(name = "setting_enable")
    private boolean enabled;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "updated_by")
    private String updatedBy;
}
