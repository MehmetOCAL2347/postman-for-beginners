package com.udemy.postman.Course.business.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseOfAllCourse {
    private String id;
    private String courseName;
    private String instructor;
    private double coursePrice;
    private String language;
    private boolean isCourseActive;
    private double starPoint;
    private int studentCount;
    private String courseUrl;
}
