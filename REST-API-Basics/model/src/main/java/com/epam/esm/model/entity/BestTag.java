package com.epam.esm.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class BestTag {

    @NotNull(message = "message.entity.data.missing")
    private Tag tag;

    @NotNull(message = "message.entity.data.missing")
    @DecimalMin(value = "0.1", inclusive = false, message = "message.besttag.price.invalid")
    private BigDecimal totalPrice;

}
