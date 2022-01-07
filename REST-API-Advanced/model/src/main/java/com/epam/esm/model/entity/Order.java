package com.epam.esm.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

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
@SuperBuilder
@AllArgsConstructor
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

}
