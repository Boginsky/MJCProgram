package com.epam.esm.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Data
@Builder
public class OAuthSignUpDto { // FIXME: 26.01.2022 

    private Map<String, String> refs = new HashMap<>();

}
