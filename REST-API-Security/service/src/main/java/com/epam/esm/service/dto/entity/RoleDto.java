package com.epam.esm.service.dto.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
public class RoleDto {

    @NotNull(message = "message.entity.data.missing")
    @Size(min = 1, max = 100, message = "message.user.status.invalid")
    private String name;

}
