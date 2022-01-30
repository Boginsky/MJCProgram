package com.epam.esm.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
@Entity
@EntityListeners(EntityListeners.class)
@Table(name = "orders")
@SuperBuilder
@AllArgsConstructor
public class Order extends ApplicationBaseEntity {

    @Column(name = "total_price", nullable = false, updatable = false)
    private BigDecimal totalPrice;

    @Column(name = "date_of_purchase", nullable = false)
    private ZonedDateTime dateOfPurchase;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "gift_certificate_id", nullable = false)
    private GiftCertificate giftCertificate;

}
