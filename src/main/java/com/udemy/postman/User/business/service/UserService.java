package com.udemy.postman.User.business.service;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;


// ReactiveUserDetailsService'e extend olmak için oluşturuldu. Geri kalan servis işlemleri AuthService'de yapılmaktadır
public interface UserService extends ReactiveUserDetailsService {

}
