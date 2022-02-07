package com.epam.esm.model.entity;

import com.epam.esm.model.audit.EntityAuditListener;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@EntityListeners(EntityAuditListener.class)
@NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
@Table(name = "gift_certificate")
@SuperBuilder
@SQLDelete(sql = "UPDATE gift_certificate SET status = false WHERE id =?")
@Where(clause = "status=true")
@AllArgsConstructor
public class GiftCertificate extends ApplicationBaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "duration", nullable = false)
    private int duration;

    @Column(name = "status", nullable = false)
    private boolean status;

    @Column(name = "create_date", nullable = false)
    private ZonedDateTime createDate;

    @Column(name = "last_update_date", nullable = false)
    private ZonedDateTime lastUpdateDate;

    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "gift_certificate_tag",
            joinColumns = @JoinColumn(name = "gift_certificate_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id")
    )
    private Set<Tag> tags = new HashSet<>();

}