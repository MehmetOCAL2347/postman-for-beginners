package com.udemy.postman.Course.entities;

import com.udemy.postman.Comment.entities.entity.Comment;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "courses")
@Data
@Builder
@EqualsAndHashCode(of="id")
public class Course {
    private String id;
    private String courseName;
    private String instructor;
    private double coursePrice;
    private String language;
    private boolean isCourseActive;
    private double starPoint;
    private int studentCount;
    private String courseUrl;
    // For Details
    private int courseDuration;
    private int lessonCount;
}
