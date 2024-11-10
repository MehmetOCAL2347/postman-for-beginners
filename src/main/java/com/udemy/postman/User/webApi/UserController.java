package com.udemy.postman.User.webApi;

import com.udemy.postman.User.business.requests.RequestOfLoginUser;
import com.udemy.postman.User.business.requests.RequestOfRegisterUser;
import com.udemy.postman.User.business.responses.ResponseOfUser;
import com.udemy.postman.User.business.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class UserController {

    /*
    register
    login
    forgot-password
    reset-password
     */

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public Mono<ResponseEntity<ResponseOfUser>> register(@RequestBody RequestOfRegisterUser request){
        return authService.registerUser(request);
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<ResponseOfUser>> login(@RequestBody RequestOfLoginUser request){
        return authService.loginUser(request);
    }
}
