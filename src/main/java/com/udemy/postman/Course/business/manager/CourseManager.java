package com.udemy.postman.Course.business.manager;

import com.udemy.postman.Comment.business.responses.ResponseOfCommentDetail;
import com.udemy.postman.Comment.dataAccess.mongo.CommentRepositoryMongo;
import com.udemy.postman.Comment.entities.entity.Comment;
import com.udemy.postman.Course.business.requests.RequestOfCourse;
import com.udemy.postman.Course.business.responses.ResponseOfAllCourse;
import com.udemy.postman.Course.business.responses.ResponseOfDetailCourse;
import com.udemy.postman.Course.business.service.CourseService;
import com.udemy.postman.Course.dataAccess.CourseRepositoryMongo;
import com.udemy.postman.Course.entities.Course;
import com.udemy.postman.User.business.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseManager implements CourseService {

    @Autowired
    private CourseRepositoryMongo courseRepositoryMongo;

    @Autowired
    private CommentRepositoryMongo commentRepositoryMongo;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    @Autowired
    private AuthService authService;

    @Override
    public Flux<ResponseOfAllCourse> getCoursesWithParams(RequestOfCourse requestOfCourse) {

        Query query = new Query();
        Criteria criteria = new Criteria();


        ///*************** Burası da busineess ruless ******************** ///

        if (requestOfCourse.getInstructor() != null && !requestOfCourse.getInstructor().isEmpty()) {
            criteria.and("instructor").in(requestOfCourse.getInstructor());
        }

        if(requestOfCourse.getIsActive() != null){
            criteria.and("isCourseActive").is(requestOfCourse.getIsActive());
        }

        if(requestOfCourse.getLanguage() != null && !requestOfCourse.getLanguage().isEmpty()){
            criteria.and("language").is(requestOfCourse.getLanguage());
        }

        if(requestOfCourse.getStarPoint() != null && !requestOfCourse.getStarPoint().isNaN()){
            criteria.and("starPoint").gte(requestOfCourse.getStarPoint()); // greater then and equals
        }

        if (requestOfCourse.getMinPriceValue() != null && requestOfCourse.getMaxPriceValue() != null) {
            criteria.and("coursePrice").gte(requestOfCourse.getMinPriceValue()).lte(requestOfCourse.getMaxPriceValue());
        } else if (requestOfCourse.getMinPriceValue() != null) {
            criteria.and("coursePrice").gte(requestOfCourse.getMinPriceValue());
        } else if (requestOfCourse.getMaxPriceValue() != null) {
            criteria.and("coursePrice").lte(requestOfCourse.getMaxPriceValue());
        }

        if (criteria.getCriteriaObject().size() > 0) {
            query.addCriteria(criteria);
        }

        return mongoTemplate.find(query, Course.class)
                .map(course -> {
                    ResponseOfAllCourse response = ResponseOfAllCourse.builder()
                            .id(course.getId())
                            .isCourseActive(course.isCourseActive())
                            .courseUrl(course.getCourseUrl())
                            .courseName(course.getCourseName())
                            .instructor(course.getInstructor())
                            .coursePrice(course.getCoursePrice())
                            .language(course.getLanguage())
                            .starPoint(course.getStarPoint())
                            .studentCount(course.getStudentCount())
                            .build();
                    return response;
                })
                //.doOnNext(course -> System.out.println("Mapped course: " + course))
                .onErrorResume(e -> {
                    //System.err.println("Error occurred while fetching courses: " + e.getMessage());
                    return Flux.empty(); // Return an empty Flux in case of error
                });
    }

    @Override
    public Mono<ResponseOfDetailCourse> getOneCourseDetail(String courseId) {

        return courseRepositoryMongo.findById(courseId)  // Returns Mono<Course>
                .flatMap(course ->
                        commentRepositoryMongo.findByCourseId(courseId)  // Returns Flux<Comment>
                                .map(this::convertToResponseOfDetailComment)  // Convert each Comment to ResponseOfDetailComment
                                .collectList()  // Collect Flux<ResponseOfDetailComment> into List<ResponseOfDetailComment>
                                .map(comments -> buildResponse(course, comments))  // Build the response
                );
    }

    @Override
    public Mono<String> getCourseNameWithId(String courseId) {
        return courseRepositoryMongo.findById(courseId).map(course -> course.getCourseName());
    }

    /**
     *
     * @param course
     * @param comments
     * @return
     * This method is convert Course object to ResponseOfDetailCourse object with using comment List and Course object.
     */
    private ResponseOfDetailCourse buildResponse(Course course, List<ResponseOfCommentDetail> comments) {
        return ResponseOfDetailCourse.builder()
                .comments(comments)
                .id(course.getId())
                .lessonCount(course.getLessonCount())
                .courseDuration(String.valueOf(course.getCourseDuration()) + " Saat")
                .instructor(course.getInstructor())
                .courseName(course.getCourseName())
                .build();
    }

    /**
     *
     * @param comment
     * @return
     * This method is convert Comment object to ResponseOfCommentDetail object
     */
    private ResponseOfCommentDetail convertToResponseOfDetailComment(Comment comment) {
        return ResponseOfCommentDetail.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .build();
    }
}
