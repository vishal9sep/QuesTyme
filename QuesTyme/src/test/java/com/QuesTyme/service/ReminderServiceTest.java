//package com.QuesTyme.service;
//
//import com.QuesTyme.entity.Interviews;
//import com.QuesTyme.entity.User;
//import com.QuesTyme.repository.InterviewRepo;
//import com.QuesTyme.repository.UserRepository;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.jupiter.api.Assertions;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.sql.DataSource;
//import java.util.Collections;
//import java.util.List;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class ReminderServiceTest {
//
//    @Autowired
//    private ReminderService reminderService;
//
//    @Autowired
//    private JavaMailSenderImpl emailSender;
//
//    @Autowired
//    private InterviewRepo interviewRepo;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private DataSource dataSource;
//
//    private JdbcTemplate jdbcTemplate;
//
//    @Before
//    public void setup() {
//        jdbcTemplate = new JdbcTemplate(dataSource);
//        jdbcTemplate.execute("DROP TABLE IF EXISTS interviews");
//        jdbcTemplate.execute("CREATE TABLE interviews (id INT PRIMARY KEY, date VARCHAR(255), startTime VARCHAR(255), meetingLink VARCHAR(255), intervieweeId INT, reminderSent BOOLEAN)");
//        jdbcTemplate.execute("DROP TABLE IF EXISTS users");
//        jdbcTemplate.execute("CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(255), email VARCHAR(255))");
//        jdbcTemplate.execute("INSERT INTO interviews VALUES (1, '2023-04-13', '10:00:00', 'https://zoom.us/j/123456789', 1, false)");
//        jdbcTemplate.execute("INSERT INTO users VALUES (1, 'John Doe', 'john.doe@example.com')");
//    }
//
//    @Test
//    public void testSendReminders() throws Exception {
//        reminderService.sendReminders();
//        List<Interviews> interviews = interviewRepo.findAll();
//        Assertions.assertEquals(1, interviews.size());
//        Interviews interview = interviews.get(0);
//        Assertions.assertEquals(false, interview.isReminderSent());
//        List<User> users = userRepository.findByIds(Collections.singletonList(interview.getIntervieweeId()));
//        Assertions.assertEquals(1, users.size());
////        User user = users.get(0);
////        Assertions.assertEquals("john.doe@example.com", user.getEmail());
//    }
//}
