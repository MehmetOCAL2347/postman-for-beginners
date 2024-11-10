package com.udemy.postman.User.business.service;

import com.udemy.postman.User.entities.entity.UserEntity;
import org.springframework.security.core.Authentication;

import java.text.ParseException;

public interface TokenService {
    String generateJwt(UserEntity userEntity);
    String generateJwt(Authentication auth);
    String getToken(String token);
    String getUserIdFromJwt(String jwt);
}
