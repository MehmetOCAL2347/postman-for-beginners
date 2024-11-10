package com.udemy.postman.User.business.manager;

import com.nimbusds.jose.KeyLengthException;
import com.udemy.postman.User.business.requests.RequestOfLoginUser;
import com.udemy.postman.User.business.requests.RequestOfRegisterUser;
import com.udemy.postman.User.business.responses.ResponseOfUser;
import com.udemy.postman.User.business.service.AuthService;
import com.udemy.postman.User.business.service.TokenService;
import com.udemy.postman.User.dataAccess.mongo.RoleRepositoryMongo;
import com.udemy.postman.User.dataAccess.mongo.UserRepositoryMongo;
import com.udemy.postman.User.entities.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthManager implements AuthService {

    @Autowired
    private UserRepositoryMongo userRepositoryMongo;

    @Autowired
    private RoleRepositoryMongo roleRepositoryMongo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ReactiveAuthenticationManager reactiveAuthenticationManager;

    @Override
    public Mono<ResponseEntity<ResponseOfUser>> registerUser(RequestOfRegisterUser request) {
        String email = request.getEmail();
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        return roleRepositoryMongo.findByAuthority("USER")
                .flatMap(role ->
                        userRepositoryMongo.findByEmail(email)
                                .flatMap(existingUserEntity -> Mono.just(
                                        ResponseEntity.badRequest().body(new ResponseOfUser("Mail adresi ile daha önce kayıt oluşturulmuş.",null))
                                ))
                                .switchIfEmpty(Mono.defer(() -> {
                                    // Create a new user
                                    UserEntity newUserEntity = UserEntity.builder()
                                            .id(newUserIdCreator())
                                            .email(email)
                                            .authorities(Set.of(role))
                                            .password(encodedPassword)
                                            .build();

                                    return userRepositoryMongo.save(newUserEntity)
                                            .map(savedUserEntity -> ResponseEntity.ok(new ResponseOfUser("Kullanıcı başarılı şekilde oluşturulmuştur.", tokenService.generateJwt(savedUserEntity))));
                                }))
                )
                .onErrorResume(e -> {
                    // Handle any errors and return a failure response
                    return Mono.just(ResponseEntity.status(500).body(new ResponseOfUser(e.getLocalizedMessage(),null)));
                });
    }

    @Override
    public Mono<ResponseEntity<ResponseOfUser>> loginUser(RequestOfLoginUser request) {

        return reactiveAuthenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()))
                .flatMap(auth -> {
                    // On successful authentication, generate the JWT token
                    String jwt = tokenService.generateJwt(auth);
                    return Mono.just(ResponseEntity.ok(new ResponseOfUser("Giriş başarılı.", jwt)));
                })
                .onErrorResume(AuthenticationException.class, e -> {
                    // Handle authentication failure
                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(new ResponseOfUser("Geçersiz mail adresi veya şifre.", null)));
                })
                .onErrorResume(KeyLengthException.class, e -> {
                    // Handle JWT token creation error
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ResponseOfUser("Giriş yapılırken bir hata oluştu", null)));
                });
    }

    @Override
    public Mono<String> getEmailFromUserId(String userId) {
        return userRepositoryMongo.findById(userId)
                .map(userEntity -> userEntity.getEmail());
    }

    private String newUserIdCreator(){
        int length = 10;
        String key_1 = "UDU_";
        String letters = "ABCDEFGHIJKLMONPRSTWQXYZ1234567890";
        StringBuilder orderCreator = new StringBuilder();
        SecureRandom rand = new SecureRandom();

        for(int i = 0; i < length; i++){
            orderCreator.append(letters.charAt(rand.nextInt(letters.length())));
        }
        return key_1 + orderCreator.toString();
    }
}
