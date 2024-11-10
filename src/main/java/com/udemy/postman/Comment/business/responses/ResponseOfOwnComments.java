package com.udemy.postman.Comment.business.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseOfOwnComments{
    private String id;
    private String comment;
    private String courseName;
}
