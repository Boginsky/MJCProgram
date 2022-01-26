package com.epam.esm.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class UserDto extends RepresentationModel<UserDto> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    @NotNull(message = "message.entity.data.missing")
    @Size(min = 1, max = 100, message = "message.user.invalid")
    private String firstName;

    @NotNull(message = "message.entity.data.missing")
    @Size(min = 1, max = 100, message = "message.user.invalid")
    private String lastName;

}



