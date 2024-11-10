package com.udemy.postman.StartUp;

import com.udemy.postman.Comment.dataAccess.mongo.CommentRepositoryMongo;
import com.udemy.postman.Comment.entities.entity.Comment;
import com.udemy.postman.Course.dataAccess.CourseRepositoryMongo;
import com.udemy.postman.Course.entities.Course;
import com.udemy.postman.User.dataAccess.mongo.RoleRepositoryMongo;
import com.udemy.postman.User.dataAccess.mongo.UserRepositoryMongo;
import com.udemy.postman.User.entities.entity.Role;
import com.udemy.postman.User.entities.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DemoDataCreator {

    @Autowired
    private CourseRepositoryMongo courseRepositoryMongo;

    @Autowired
    private CommentRepositoryMongo commentRepositoryMongo;

    @Autowired
    private UserRepositoryMongo userRepositoryMongo;

    @Autowired
    private RoleRepositoryMongo roleRepositoryMongo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String DEFAULT_ROLE_1 = "ADMIN";
    private final String DEFAULT_ROLE_2 = "USER";


    @EventListener(ApplicationReadyEvent.class)
    public void createDemoData() {

        //***************** Demo Role Creation ***************************//

        roleRepositoryMongo.findByAuthority(DEFAULT_ROLE_1)
                .switchIfEmpty(Mono.defer(() -> {
                    // Role does not exist, create a new role
                    Role newRole = new Role(DEFAULT_ROLE_1);
                    return roleRepositoryMongo.save(newRole);
                }))
                .subscribe();

        roleRepositoryMongo.findByAuthority(DEFAULT_ROLE_2)
                .switchIfEmpty(Mono.defer(() -> {
                    // Role does not exist, create a new role
                    Role newRole = new Role(DEFAULT_ROLE_2);
                    return roleRepositoryMongo.save(newRole);
                }))
                .subscribe();

        ///**************** User Save ******************************//

        roleRepositoryMongo.findByAuthority("USER").subscribe(
                role -> {
                    UserEntity user_1 = UserEntity.builder()
                            .id("UDEMY_USER_00000001")
                            .authorities(Set.of(role))
                            .email("udemy.course@gmail.com")
                            .password(passwordEncoder.encode("udemy.course"))
                            .build();

                    UserEntity user_2 = UserEntity.builder()
                            .id("UDEMY_USER_00000002")
                            .authorities(Set.of(role))
                            .email("udemy.course_v2@gmail.com")
                            .password(passwordEncoder.encode("udemy.course"))
                            .build();

                    userRepositoryMongo.save(user_1).subscribe();
                    userRepositoryMongo.save(user_2).subscribe();
                }
        );

        ///**************** Comment Save ***************************//

        Comment comment_1 = Comment.builder()
                .id("UDEMY_COMMENT_00000001")
                .comment("Başarılı ve akıcı bir anlatım. Memnun kaldım")
                .courseId("UDEMY_COURSE_00000001")
                .userId("UDEMY_USER_00000001")
                .build();

        Comment comment_2 = Comment.builder()
                .id("UDEMY_COMMENT_00000002")
                .comment("Daha hızlı anlatılabilirdi. 1.5 hızda izleyin")
                .courseId("UDEMY_COURSE_00000001")
                .userId("UDEMY_USER_00000001")
                .build();

        Comment comment_3 = Comment.builder()
                .id("UDEMY_COMMENT_00000003")
                .comment("Sorularıma hızlı yanıtlar alabildim. Teşekkür ederim")
                .courseId("UDEMY_COURSE_00000001")
                .userId("UDEMY_USER_00000001")
                .build();

        Comment comment_4 = Comment.builder()
                .id("UDEMY_COMMENT_00000004")
                .comment("Ses ve video kalitesi oldukça başarılı")
                .courseId("UDEMY_COURSE_00000002")
                .userId("UDEMY_USER_00000002")
                .build();

        Comment comment_5 = Comment.builder()
                .id("UDEMY_COMMENT_00000005")
                .comment("Yorumları okuyarak aldım. Başlangıç yapmak isteyen herkese tavsiye ediyorum")
                .courseId("UDEMY_COURSE_00000002")
                .userId("UDEMY_USER_00000002")
                .build();

        Comment comment_6 = Comment.builder()
                .id("UDEMY_COMMENT_00000006")
                .comment("Daha fazla örnek yapılabilirdi. İçerik biraz kısa olmuş")
                .courseId("UDEMY_COURSE_00000002")
                .userId("UDEMY_USER_00000002")
                .build();

        commentRepositoryMongo.save(comment_1).block();
        commentRepositoryMongo.save(comment_2).block();
        commentRepositoryMongo.save(comment_3).block();
        commentRepositoryMongo.save(comment_4).block();
        commentRepositoryMongo.save(comment_5).block();
        commentRepositoryMongo.save(comment_6).block();


        ///**************** Course Save ****************************///
        Course course_1 = Course.builder()
                .id("UDEMY_COURSE_00000001")
                .courseName("Java İle Selenium Webdriver Başlangıç Eğitimi")
                .coursePrice(269.99)
                .instructor("Mehmet ÖCAL")
                .isCourseActive(true)
                .language("Java")
                .starPoint(4.77)
                .studentCount(250)
                .courseUrl("https://www.udemy.com/course/java-ile-selenium-webdriver-baslangc-egitimi/?referralCode=BAFDFC4693E816A2E591")
                .courseDuration(4)
                .lessonCount(25)
                .build();

        Course course_2 = Course.builder()
                .id("UDEMY_COURSE_00000002")
                .courseName("Java Swing İle A'dan Z'ye Gelişmiş Arayüz Tasarımı")
                .coursePrice(299.99)
                .instructor("Mehmet ÖCAL")
                .isCourseActive(true)
                .language("Java")
                .starPoint(4.57)
                .studentCount(1000)
                .courseUrl("https://www.udemy.com/course/java-swing-ile-adan-zye-gelismis-arayuz-tasarm/?referralCode=82866FE5A2828F91051D")
                .courseDuration(130)
                .lessonCount(188)
                .build();

        courseRepositoryMongo.save(course_1).block().getId();
        courseRepositoryMongo.save(course_2).block().getId();

    }

    private String commentCreator(Comment comment){
        return commentRepositoryMongo.save(comment).block().getId();
    }
}
