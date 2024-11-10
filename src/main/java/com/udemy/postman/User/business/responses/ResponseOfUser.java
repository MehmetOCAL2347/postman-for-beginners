package com.udemy.postman.User.business.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseOfUser {
    private String message;
    private String accessToken;
}
