package com.udemy.postman.User.dataAccess.mongo;

import com.udemy.postman.User.entities.entity.Role;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RoleRepositoryMongo extends ReactiveMongoRepository<Role, String> {
    Mono<Role> findByAuthority(String authority);
}
