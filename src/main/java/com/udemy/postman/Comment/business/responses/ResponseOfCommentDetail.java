package com.udemy.postman.Comment.business.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseOfCommentDetail {
    private String id;
    private String comment;
}
