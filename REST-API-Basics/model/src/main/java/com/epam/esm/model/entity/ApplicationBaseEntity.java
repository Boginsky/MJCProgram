package com.epam.esm.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class ApplicationBaseEntity {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private long id;

    public ApplicationBaseEntity() {
    }

    public ApplicationBaseEntity(long id) {
        this.id = id;
    }

    public long getId() {
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
        ApplicationBaseEntity that = (ApplicationBaseEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hash(id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AbstractEntity{");
        sb.append("id=").append(id);
        sb.append('}');
        return sb.toString();
    }
}
