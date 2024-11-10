package com.udemy.postman.Course.dataAccess;


import com.udemy.postman.Course.entities.Course;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepositoryMongo extends ReactiveMongoRepository<Course, String> {
}
