package com.epam.esm.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class GiftCertificate extends ApplicationBaseEntity {

    private String name;
    private String description;
    private BigDecimal price;
    private int duration;

    @Builder
    public GiftCertificate(long id, String name, String description, BigDecimal price, int duration, ZonedDateTime createDate, ZonedDateTime lastUpdateDate) {
        super(id);
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private ZonedDateTime createDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private ZonedDateTime lastUpdateDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftCertificate giftCertificate = (GiftCertificate) o;
        return name.equals(giftCertificate.name) &&
                description.equals(giftCertificate.description) &&
                price.equals(giftCertificate.price) &&
                Objects.equals(duration, giftCertificate.duration) &&
                createDate.equals(giftCertificate.createDate) &&
                lastUpdateDate.equals(giftCertificate.lastUpdateDate);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + ((name == null) ? 0 : name.hashCode());
        result = 31 * result + ((description == null) ? 0 : description.hashCode());
        result = 31 * result + ((price == null) ? 0 : price.hashCode());
        result = 31 * result + ((createDate == null) ? 0 : createDate.hashCode());
        result = 31 * result + ((lastUpdateDate == null) ? 0 : lastUpdateDate.hashCode());
        result = 31 * result + duration;
        return result;
    }
}