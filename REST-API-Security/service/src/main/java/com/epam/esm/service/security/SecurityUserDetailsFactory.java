package com.epam.esm.service.security;

import com.epam.esm.model.entity.Role;
import com.epam.esm.model.entity.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class SecurityUserDetailsFactory {

    public static SecurityUserDetails create(User user) {
        return new SecurityUserDetails(
                user.getUsername(),
                user.getPassword(),
                mapToGrantedAuthorities(user.getRole()),
                true
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(Role role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Set<GrantedAuthority> grantedAuthorities = RoleAuthority.valueOf(role.toString()).getAuthorities();
        authorities.addAll(grantedAuthorities);
        return authorities;
    }
}
