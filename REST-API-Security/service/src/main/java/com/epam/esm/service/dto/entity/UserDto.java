package com.epam.esm.service.dto.entity;

import com.epam.esm.model.entity.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Data
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

    @NotNull(message = "message.entity.data.missing")
    @Size(min = 1, max = 100, message = "message.user.invalid")
    private String username;

    @NotNull(message = "message.entity.data.missing")
    @Size(min = 1, max = 100, message = "message.user.invalid")
    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$", message = "message.email.invalid")
    private String email;

    @NotNull(message = "message.entity.data.missing")
    @Size(min = 1, max = 100, message = "message.user.invalid")
    private String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Role role;

}



