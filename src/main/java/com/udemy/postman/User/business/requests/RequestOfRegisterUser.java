package com.udemy.postman.User.business.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestOfRegisterUser {
    private String email;
    private String password;
}
