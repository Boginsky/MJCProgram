package com.epam.esm.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class GiftCertificateDto extends RepresentationModel<GiftCertificateDto> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "message.entity.data.missing")
    @Size(min = 1, max = 80, message = "certificate.name.invalid")
    private String name;

    @NotNull(message = "message.entity.data.missing")
    @Size(min = 1, max = 200, message = "message.certificate.description.invalid")
    private String description;

    @NotNull(message = "message.entity.data.missing")
    @DecimalMin(value = "0.1", inclusive = false, message = "message.certificate.price.invalid")
    @Digits(integer = 9, fraction = 4, message = "message.certificate.price.invalid")
    private BigDecimal price;

    @NotNull(message = "message.message.entity.data.missing")
    @Min(value = 1, message = "message.certificate.duration.invalid")
    private int duration;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private ZonedDateTime createDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private ZonedDateTime lastUpdateDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TagDto> certificateTags;

    public void addTag(TagDto tag) {
        this.certificateTags.add(tag);
    }

}

