package com.epam.esm.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class GiftCertificate extends ApplicationBaseEntity {

    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private ZonedDateTime createDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private ZonedDateTime lastUpdateDate;

    @JsonCreator
    public GiftCertificate() {
    }

    public GiftCertificate(Long id,String name, String description, BigDecimal price,
                           Integer duration, ZonedDateTime createDate, ZonedDateTime lastUpdateDate) {
        super(id);
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public ZonedDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(ZonedDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftCertificate giftCertificate = (GiftCertificate) o;
        return name.equals(giftCertificate.name) &&
                description.equals(giftCertificate.description) &&
                price.equals(giftCertificate.price) &&
                duration.equals(giftCertificate.duration) &&
                createDate.equals(giftCertificate.createDate) &&
                lastUpdateDate.equals(giftCertificate.lastUpdateDate);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + ((name == null) ? 0 : name.hashCode());
        result = 31 * result + ((description == null) ? 0 : description.hashCode());
        result = 31 * result + ((price == null) ? 0 : price.hashCode());
        result = 31 * result + ((duration == null) ? 0 : duration.hashCode());
        result = 31 * result + ((createDate == null) ? 0 : createDate.hashCode());
        result = 31 * result + ((lastUpdateDate == null) ? 0 : lastUpdateDate.hashCode());
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GiftCertificate{");
        sb.append("name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", price=").append(price);
        sb.append(", duration=").append(duration);
        sb.append(", createDate=").append(createDate);
        sb.append(", lastUpdateDate=").append(lastUpdateDate);
        sb.append('}');
        return sb.toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final GiftCertificate giftCertificate;

        public Builder() {
            giftCertificate = new GiftCertificate();
        }

        public Builder setId(Long id) {
            giftCertificate.setId(id);
            return this;
        }

        public Builder setName(String name) {
            giftCertificate.setName(name);
            return this;
        }

        public Builder setDescription(String description) {
            giftCertificate.setDescription(description);
            return this;
        }

        public Builder setPrice(BigDecimal price) {
            giftCertificate.setPrice(price);
            return this;
        }

        public Builder setDuration(Integer duration) {
            giftCertificate.setDuration(duration);
            return this;
        }

        public Builder setCreateTime(ZonedDateTime createTime) {
            giftCertificate.setCreateDate(createTime);
            return this;
        }

        public Builder setLastUpdateDate(ZonedDateTime lastUpdateDate) {
            giftCertificate.setLastUpdateDate(lastUpdateDate);
            return this;
        }

        public GiftCertificate build() {
            return giftCertificate;
        }
    }
}
