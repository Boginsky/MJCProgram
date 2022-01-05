package com.epam.esm.service.validator;

import com.epam.esm.service.dto.OrderDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("orderValidator")
public class OrderValidator extends AbstractValidator<OrderDto> {

    protected OrderValidator() {
        super(OrderDto.class);
    }

    public void validatePrice(OrderDto orderDto) {
        BigDecimal totalPrice = orderDto.getTotalPrice();
        if (totalPrice != null) {
            validateField(TOTAL_PRICE, totalPrice);
        }
    }
}
