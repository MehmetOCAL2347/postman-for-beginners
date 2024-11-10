package com.udemy.postman.Comment.webApi;

import com.udemy.postman.Comment.business.requests.RequestOfEditComment;
import com.udemy.postman.Comment.business.requests.RequestOfNewComment;
import com.udemy.postman.Comment.business.responses.ResponseOfCommentDetail;
import com.udemy.postman.Comment.business.responses.ResponseOfOwnComments;
import com.udemy.postman.Comment.business.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CommentController {

    // ******************  Authentication gerekecek ************************ //
    // Kurs id'si ile yorum oku
    // Yorum güncelle
    // Yorum sil

    @Autowired
    private CommentService commentService;

    @PostMapping("/saveNewComment")
    public Mono<ResponseEntity<String>> saveNewComment( @RequestHeader(name = "Authorization") String token, @RequestBody RequestOfNewComment request){
        return commentService.saveNewComment(token, request);
    }

    @GetMapping("/getYourAllComments")
    public Flux<ResponseOfOwnComments> getYourAllComments(@RequestHeader(name = "Authorization") String token){
        return commentService.getYourAllComments(token);
    }

    @DeleteMapping("/deleteAComment/{commentId}")
    public Mono<ResponseEntity<String>> deleteAComment(@RequestHeader(name = "Authorization") String token, @PathVariable String commentId){
        return commentService.deleteAComment(token, commentId);
    }

    @PatchMapping("/editComment/{commentId}")
    public Mono<ResponseEntity<String>> editComment(@RequestHeader(name = "Authorization") String token, @PathVariable String commentId, @RequestBody RequestOfEditComment request){
        return commentService.editComment(token, commentId, request);
    }

}
