package com.udemy.postman.Course.webApi;

import com.udemy.postman.Course.business.requests.RequestOfCourse;
import com.udemy.postman.Course.business.responses.ResponseOfAllCourse;
import com.udemy.postman.Course.business.responses.ResponseOfDetailCourse;
import com.udemy.postman.Course.business.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public Flux<ResponseOfAllCourse> getCoursesWithParams(@ModelAttribute RequestOfCourse request){
        return courseService.getCoursesWithParams(request);
    }

    @GetMapping("/courseDetail/{courseId}")
    public Mono<ResponseOfDetailCourse> getOneCourseDetail(@PathVariable("courseId") String courseId){
        return courseService.getOneCourseDetail(courseId);
    }

}
