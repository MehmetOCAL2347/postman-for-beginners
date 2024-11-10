package com.udemy.postman.Comment.dataAccess.mongo;

import com.udemy.postman.Comment.entities.entity.Comment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface CommentRepositoryMongo extends ReactiveMongoRepository<Comment, String> {
    Flux<Comment> findByCourseId(String courseId);
    Flux<Comment> findAllByUserId(String userId);
}
