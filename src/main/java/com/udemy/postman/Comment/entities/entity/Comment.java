package com.udemy.postman.Comment.entities.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "comments")
@Data
@Builder
@EqualsAndHashCode(of="id")
public class Comment {
    private String id;
    private String comment;
    private String courseId;
    private String userId;
}
