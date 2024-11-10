package com.udemy.postman.Comment.business.service;

import com.udemy.postman.Comment.business.requests.RequestOfEditComment;
import com.udemy.postman.Comment.business.requests.RequestOfNewComment;
import com.udemy.postman.Comment.business.responses.ResponseOfOwnComments;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommentService {
    Mono<ResponseEntity<String>> saveNewComment(String token, RequestOfNewComment request);
    Mono<ResponseEntity<String>> deleteAComment(String token, String commentId);
    Mono<ResponseEntity<String>> editComment(String token, String commentId, RequestOfEditComment request);
    Flux<ResponseOfOwnComments> getYourAllComments(String token);
}
