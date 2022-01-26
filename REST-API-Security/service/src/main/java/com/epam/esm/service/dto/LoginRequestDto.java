package com.epam.esm.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Data
@Builder
public class LoginRequestDto {

    @Size(min = 1, max = 300, message = "message.user.username.invalid")
    private String username;

    @Size(min = 1, max = 100, message = "message.user.password.invalid")
    private String password;

}
