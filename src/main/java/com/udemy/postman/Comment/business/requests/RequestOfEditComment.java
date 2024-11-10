package com.udemy.postman.Comment.business.requests;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestOfEditComment {
    private String newComment;
}
