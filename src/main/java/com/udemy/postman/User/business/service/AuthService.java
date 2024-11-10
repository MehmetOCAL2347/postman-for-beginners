package com.udemy.postman.User.business.service;

import com.udemy.postman.User.business.requests.RequestOfLoginUser;
import com.udemy.postman.User.business.requests.RequestOfRegisterUser;
import com.udemy.postman.User.business.responses.ResponseOfUser;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<ResponseEntity<ResponseOfUser>> registerUser(RequestOfRegisterUser request);
    Mono<ResponseEntity<ResponseOfUser>> loginUser(RequestOfLoginUser request);
    Mono<String> getEmailFromUserId(String userId);
}
