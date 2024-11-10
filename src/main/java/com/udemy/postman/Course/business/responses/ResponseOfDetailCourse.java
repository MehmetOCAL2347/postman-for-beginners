package com.udemy.postman.Course.business.responses;

import com.udemy.postman.Comment.business.responses.ResponseOfCommentDetail;
import com.udemy.postman.Comment.entities.entity.Comment;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseOfDetailCourse{
    private String id;
    private String courseName;
    private String instructor;
    private String courseDuration;
    private int lessonCount;
    private List<ResponseOfCommentDetail> comments;
}
