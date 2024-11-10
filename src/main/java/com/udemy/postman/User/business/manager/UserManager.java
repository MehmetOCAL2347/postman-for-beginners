package com.udemy.postman.User.business.manager;

import com.udemy.postman.User.business.service.UserService;
import com.udemy.postman.User.dataAccess.mongo.UserRepositoryMongo;
import com.udemy.postman.User.entities.entity.Role;
import com.udemy.postman.User.entities.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserManager implements UserService {

    @Autowired
    private UserRepositoryMongo userRepositoryMongo;

    @Override
    public Mono<UserDetails> findByUsername(String email) { // UserService -> ReactiveUserService'den implemente ettik
        return userRepositoryMongo.findByEmail(email)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("Kullanıcı Bulunamadı. Email Adresinizi Kontrol ediniz: " + email)))
                .map(user -> UserEntity.builder()
                        .id(user.getId())
                        .password(user.getPassword())
                        .authorities(user.getAuthorities())
                        .email(user.getEmail())
                        /*.withUsername(user.getEmail()) // method ismi username ama email ile de çalışcak mı acaba?
                        .password(user.getPassword())
                        .authorities("USER")
                        */
                        .build());
    }
}
