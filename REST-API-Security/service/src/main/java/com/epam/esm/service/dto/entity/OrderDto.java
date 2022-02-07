package com.epam.esm.service.dto.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Data
@Builder
public class OrderDto extends RepresentationModel<OrderDto> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private GiftCertificateDto giftCertificate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UserDto user;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @DecimalMin(value = "0.1", inclusive = false, message = "message.order.price.invalid")
    private BigDecimal totalPrice;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private ZonedDateTime dateOfPurchase;
}
