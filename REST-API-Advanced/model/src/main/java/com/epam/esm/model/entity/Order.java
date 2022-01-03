package com.epam.esm.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
@Entity
@EntityListeners(EntityListeners.class)
@Table(name = "orders")
public class Order extends ApplicationBaseEntity {

    @Column(name = "total_price", nullable = false, updatable = false)
    private BigDecimal totalPrice;

    @Column(name = "date_of_purchase", nullable = false)
    private ZonedDateTime dateOfPurchase;

    @JsonIgnore
    @ManyToOne(targetEntity = User.class)
    private User user;

    @JsonIgnore
    @ManyToOne(targetEntity = GiftCertificate.class)
    private GiftCertificate giftCertificate;

    @Builder
    public Order(long id, BigDecimal totalPrice, ZonedDateTime dateOfPurchase, User user, GiftCertificate giftCertificate) {
        super(id);
        this.totalPrice = totalPrice;
        this.dateOfPurchase = dateOfPurchase;
        this.user = user;
        this.giftCertificate = giftCertificate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return totalPrice.equals(order.totalPrice)
                && dateOfPurchase.equals(dateOfPurchase)
                && user.equals(user)
                && giftCertificate.equals(giftCertificate);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + ((totalPrice == null) ? 0 : totalPrice.hashCode());
        result = 31 * result + ((dateOfPurchase == null) ? 0 : dateOfPurchase.hashCode());
        result = 31 * result + ((user == null) ? 0 : user.hashCode());
        result = 31 * result + ((giftCertificate == null) ? 0 : giftCertificate.hashCode());
        return result;
    }
}
