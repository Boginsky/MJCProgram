package com.epam.esm.service.dto;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class UserDto extends RepresentationModel<UserDto> {

    @NotNull(message = "message.entity.data.missing")
    private User user;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<GiftCertificate> giftCertificateList;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Order> orderList;

}



