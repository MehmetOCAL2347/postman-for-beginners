package com.udemy.postman.Course.business.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestOfCourse {
    private Boolean isActive;
    private Double starPoint;
    private String language;
    private String instructor;
    private Double minPriceValue;
    private Double maxPriceValue;
}
