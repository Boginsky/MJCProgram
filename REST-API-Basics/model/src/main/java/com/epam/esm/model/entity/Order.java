package com.epam.esm.model.entity;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
public class Order extends ApplicationBaseEntity {

    private BigDecimal totalPrice;
    private ZonedDateTime dateOfPurchase;
    private User user;
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
