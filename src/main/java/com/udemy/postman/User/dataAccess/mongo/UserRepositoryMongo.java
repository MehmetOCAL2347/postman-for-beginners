package com.udemy.postman.User.dataAccess.mongo;

import com.udemy.postman.User.entities.entity.UserEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepositoryMongo extends ReactiveMongoRepository<UserEntity, String> {
    Mono<UserEntity> findByEmail(String email);
}
