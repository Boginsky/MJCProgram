package com.epam.esm.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Tag extends AbstractEntity {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String name;

    public Tag() {
    }

    public Tag(Long id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return name.equals(tag.name);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Tag{");
        sb.append("name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final Tag tag;

        public Builder() {
            tag = new Tag();
        }

        public Builder setId(Long id) {
            tag.setId(id);
            return this;
        }

        public Builder setName(String name) {
            tag.setName(name);
            return this;
        }

        public Tag build() {
            return tag;
        }
    }
}
