package com.udemy.postman.User.entities.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

@Document(collection = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(of="id")
public class Role implements GrantedAuthority {

    private String id;
    private String authority;

    public Role(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
