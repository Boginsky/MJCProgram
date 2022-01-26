package com.epam.esm.service.util.validator;

import com.epam.esm.service.dto.entity.GiftCertificateDto;
import com.epam.esm.service.dto.entity.TagDto;
import com.epam.esm.service.exception.InvalidEntityException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component("giftCertificateValidator")
public class GiftCertificateValidator extends AbstractValidator<GiftCertificateDto> {

    protected GiftCertificateValidator() {
        super(GiftCertificateDto.class);
    }

    public void validateDuration(GiftCertificateDto giftCertificateDto) {
        int duration = giftCertificateDto.getDuration();
        if (duration != 0) {
            validateField(DURATION, duration);
        } else {
            throw new InvalidEntityException("message.validation.fail");
        }
    }

    public void validatePrice(GiftCertificateDto giftCertificateDto) {
        BigDecimal price = giftCertificateDto.getPrice();
        if (price != null) {
            validateField(PRICE, price);
        } else {
            throw new InvalidEntityException("message.validation.fail");
        }
    }

    public void validateDescription(GiftCertificateDto giftCertificateDto) {
        String description = giftCertificateDto.getDescription();
        if (description != null) {
            validateField(DESCRIPTION, description);
        } else {
            throw new InvalidEntityException("message.validation.fail");
        }
    }

    public void validateName(GiftCertificateDto giftCertificateDto) {
        String name = giftCertificateDto.getName();
        if (name != null) {
            validateField(NAME, name);
        } else {
            throw new InvalidEntityException("message.validation.fail");
        }
    }

    public void validateTags(GiftCertificateDto giftCertificateDto) {
        Set<TagDto> dtoTags = giftCertificateDto.getTags();
        if (dtoTags != null) {
            dtoTags.forEach(tag -> {
                if (!validator.validate(tag).isEmpty()) {
                    throw new InvalidEntityException("tag.invalid");
                }
            });
        }
    }
}
