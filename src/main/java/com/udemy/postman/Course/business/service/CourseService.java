package com.udemy.postman.Course.business.service;

import com.udemy.postman.Course.business.requests.RequestOfCourse;
import com.udemy.postman.Course.business.responses.ResponseOfAllCourse;
import com.udemy.postman.Course.business.responses.ResponseOfDetailCourse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CourseService {
    Flux<ResponseOfAllCourse> getCoursesWithParams(RequestOfCourse requestOfCourse);
    Mono<ResponseOfDetailCourse> getOneCourseDetail(String courseId);
    Mono<String> getCourseNameWithId(String courseId);
}
