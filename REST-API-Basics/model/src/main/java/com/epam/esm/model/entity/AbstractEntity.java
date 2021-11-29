package com.epam.esm.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class AbstractEntity {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonCreator
    public AbstractEntity(){
    }

    public AbstractEntity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractEntity that = (AbstractEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 31 * (id == null ? 0 : id.hashCode());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AbstractEntity{");
        sb.append("id=").append(id);
        sb.append('}');
        return sb.toString();
    }
}
