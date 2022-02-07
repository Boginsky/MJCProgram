package com.epam.esm.service.dto;

import com.epam.esm.model.entity.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Data
@Builder
public class GeneratedJwtDto extends RepresentationModel<GeneratedJwtDto> {

    private String username;
    private Role role;
    private String jwt;
    private String jwtRefresh;

}
