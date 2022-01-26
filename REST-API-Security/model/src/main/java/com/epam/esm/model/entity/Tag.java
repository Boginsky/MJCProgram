package com.epam.esm.model.entity;

import com.epam.esm.model.audit.EntityAuditListener;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
@Entity
@Table(name = "tag")
@EntityListeners(EntityAuditListener.class)
@SuperBuilder
@AllArgsConstructor
@NamedNativeQuery(
        name = "findMostWidelyUsedTagWithHighestOrderCostByUserId",
        query = "SELECT t.id as 'id', t.name as 'tagName', MAX(o.total_price) as 'highestCost'\n" +
                "FROM orders o\n" +
                "JOIN gift_certificate_tag ct on o.gift_certificate_id = ct.gift_certificate_id\n" +
                "JOIN tag t on ct.tag_id = t.id\n" +
                "WHERE o.user_id = :userId\n" +
                "GROUP BY t.id\n" +
                "ORDER BY COUNT(t.id) DESC, MAX(o.total_price) DESC\n" +
                "LIMIT 1",
        resultSetMapping = "BestTagMapping"
)
public class Tag extends ApplicationBaseEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "tags", cascade = CascadeType.PERSIST)
    private List<GiftCertificate> giftCertificateList = new ArrayList<>();

}