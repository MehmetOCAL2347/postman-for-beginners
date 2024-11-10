package com.udemy.postman.Comment.business.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestOfNewComment {
    private String courseId;
    private String comment;
}
