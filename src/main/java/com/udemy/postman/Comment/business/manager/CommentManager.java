package com.udemy.postman.Comment.business.manager;

import com.udemy.postman.Comment.business.requests.RequestOfEditComment;
import com.udemy.postman.Comment.business.requests.RequestOfNewComment;
import com.udemy.postman.Comment.business.responses.ResponseOfCommentDetail;
import com.udemy.postman.Comment.business.responses.ResponseOfOwnComments;
import com.udemy.postman.Comment.business.service.CommentService;
import com.udemy.postman.Comment.dataAccess.mongo.CommentRepositoryMongo;
import com.udemy.postman.Comment.entities.entity.Comment;
import com.udemy.postman.Course.business.service.CourseService;
import com.udemy.postman.User.business.service.AuthService;
import com.udemy.postman.User.business.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class CommentManager implements CommentService {

    @Autowired
    private CommentRepositoryMongo commentRepositoryMongo;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CourseService courseService;

    @Override
    public Mono<ResponseEntity<String>> saveNewComment(String token, RequestOfNewComment request) {

        String splittedToken = tokenService.getToken(token);

        return commentRepositoryMongo.save(
                        Comment.builder()
                                .id(newCommentIdCreator())
                                .courseId(request.getCourseId())
                                .comment(request.getComment())
                                .userId(tokenService.getUserIdFromJwt(splittedToken))
                                .build())
                .map(savedComment -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body("Yorumunuz başarılı şekilde kaydedildi. ID: " + savedComment.getId()))
                .defaultIfEmpty(ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Yorumunuz kaydedilirken hata oluştu.")
                );
    }

    @Override
    public Mono<ResponseEntity<String>> deleteAComment(String token, String commentId) {

        String splittedToken = tokenService.getToken(token);
        String userId = tokenService.getUserIdFromJwt(splittedToken);

        return commentRepositoryMongo.findById(commentId)
                .flatMap(existingComment -> {
                    if (existingComment.getUserId().equals(userId)) {
                        return commentRepositoryMongo.delete(existingComment)
                                .then(Mono.just(ResponseEntity
                                        .status(HttpStatus.OK)
                                        .body("Yorumunuz başarılı şekilde silindi.")));
                    } else {
                        return Mono.just(ResponseEntity
                                .status(HttpStatus.FORBIDDEN)
                                .body("Bu yorum size ait olmadığı için silemezsiniz"));
                    }
                })
                .switchIfEmpty(Mono.just(ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Silmek istediğiniz yorum bulunamamıştır. " + commentId)));
    }

    @Override
    public Mono<ResponseEntity<String>> editComment(String token, String commentId, RequestOfEditComment request) {

        String splittedToken = tokenService.getToken(token);
        String userId = tokenService.getUserIdFromJwt(splittedToken);

        return commentRepositoryMongo.findById(commentId)
                .flatMap(existingComment -> {

                    if (existingComment.getUserId().equals(userId) && request.getNewComment() != null && !request.getNewComment().isEmpty()) {

                        existingComment.setComment(request.getNewComment());

                        return commentRepositoryMongo.save(existingComment)
                                .then(Mono.just(ResponseEntity
                                        .status(HttpStatus.OK)
                                        .body("Yorumunuz başarılı şekilde güncellendi.")));

                    } else {
                        return Mono.just(ResponseEntity
                                .status(HttpStatus.FORBIDDEN)
                                .body("Bu yorum size ait olmadığı için güncelleyemezsiniz."));
                    }


                    /*

                    if (request.getNewComment() != null && !request.getNewComment().isEmpty()) {
                        existingComment.setComment(request.getNewComment());
                    }

                    return commentRepositoryMongo.save(existingComment)
                            .then(Mono.just(ResponseEntity.ok("Yorumunuz başarılı şekilde güncellendi.")));

                     */
                })
                .switchIfEmpty(Mono.just(ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Güncellemek istediğiniz yorum bulunamadı.")))
                .onErrorResume(e -> Mono.just(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Yorum Güncellenirken bir hata oluştu." + e.getMessage())));
    }

    @Override
    public Flux<ResponseOfOwnComments> getYourAllComments(String token) {

        String splittedToken = tokenService.getToken(token);
        String userId = tokenService.getUserIdFromJwt(splittedToken);

        return commentRepositoryMongo.findAllByUserId(userId)
                .flatMapSequential(comment -> {
                    return courseService.getCourseNameWithId(comment.getCourseId())
                            .defaultIfEmpty("Kurs bulunamadı")
                            .map(courseName -> {
                                return ResponseOfOwnComments.builder()
                                        .id(comment.getId())
                                        .comment(comment.getComment())
                                        .courseName(courseName)
                                        .build();
                            });
                })
                .onErrorResume(e -> {
                    return Flux.empty();
                });
    }

    private String newCommentIdCreator() {
        int length = 10;
        String key_1 = "UDC_";
        String letters = "ABCDEFGHIJKLMONPRSTWQXYZ1234567890";
        StringBuilder orderCreator = new StringBuilder();
        SecureRandom rand = new SecureRandom();

        for (int i = 0; i < length; i++) {
            orderCreator.append(letters.charAt(rand.nextInt(letters.length())));
        }
        return key_1 + orderCreator.toString();
    }

}
