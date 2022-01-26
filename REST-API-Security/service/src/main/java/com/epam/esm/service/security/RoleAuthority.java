package com.epam.esm.service.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.epam.esm.service.security.Permission.BEST_TAG_GET;
import static com.epam.esm.service.security.Permission.GIFT_CERTIFICATES_CREATE;
import static com.epam.esm.service.security.Permission.GIFT_CERTIFICATES_DELETE;
import static com.epam.esm.service.security.Permission.GIFT_CERTIFICATES_GET;
import static com.epam.esm.service.security.Permission.GIFT_CERTIFICATES_UPDATE;
import static com.epam.esm.service.security.Permission.ORDERS_CREATE;
import static com.epam.esm.service.security.Permission.ORDERS_GET;
import static com.epam.esm.service.security.Permission.TAGS_CREATE;
import static com.epam.esm.service.security.Permission.TAGS_DELETE;
import static com.epam.esm.service.security.Permission.TAGS_GET;
import static com.epam.esm.service.security.Permission.TAGS_UPDATE;
import static com.epam.esm.service.security.Permission.USERS_GET;

public enum RoleAuthority {

    USER(permissionSetOf(
            ORDERS_GET, TAGS_GET, USERS_GET, BEST_TAG_GET, GIFT_CERTIFICATES_GET, ORDERS_CREATE)),
    ADMIN(permissionSetOf(
            ORDERS_GET, ORDERS_CREATE, BEST_TAG_GET, TAGS_GET, TAGS_CREATE, TAGS_DELETE, USERS_GET, TAGS_UPDATE,
            GIFT_CERTIFICATES_GET, GIFT_CERTIFICATES_CREATE, GIFT_CERTIFICATES_DELETE, GIFT_CERTIFICATES_UPDATE));

    private final Set<Permission> permissionsSet;

    RoleAuthority(Set<Permission> permissionsSet) {
        this.permissionsSet = permissionsSet;
    }

    public Set<Permission> getPermissionsSet() {
        return permissionsSet;
    }

    public Set<GrantedAuthority> getAuthorities() {
        return getPermissionsSet().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getAdmission()))
                .collect(Collectors.toSet());
    }

    private static Set<Permission> permissionSetOf(Permission... permissions) {
        return new HashSet<>(Arrays.asList(permissions));
    }
}
