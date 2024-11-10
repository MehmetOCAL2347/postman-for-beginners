package com.udemy.postman.User.entities.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class UserEntity implements UserDetails {
    private String id;
    private String email;
    private String password;
    private Set<Role> authorities;
    private boolean emailVerify = false; // default = false olmalı
    // resetToken eklencek -> şifremi unuttum kısmı yapılmalı !!


    /*@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream()
                .map(role -> (GrantedAuthority) role) // Ensure Role implements GrantedAuthority
                .collect(Collectors.toSet());
    }*/

    @Override
    public Set<Role> getAuthorities() {
        return authorities;  // No need for casting, Role already implements GrantedAuthority
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Implement your logic here
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implement your logic here
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implement your logic here
    }

    @Override
    public boolean isEnabled() {
        return true; // Implement your logic here
    }

}
