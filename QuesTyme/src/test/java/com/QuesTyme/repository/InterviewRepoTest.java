package com.QuesTyme.repository;
import com.QuesTyme.entity.Interviews;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class InterviewRepoTest {

    @Autowired
    private InterviewRepo interviewRepo;

    @Test
    public void testFindByInterview() {
        // create test data
        Interviews interview = new Interviews();
        interview.setIntervieweeId(30);
        interview.setInterviewerId(31);
        interview.setDate(LocalDate.now());
        interview.setStartTime(LocalTime.of(9, 0,0));
        interview.setEndTime(LocalTime.of(10, 0,0));
        interviewRepo.save(interview);

        // call the method being tested
        List<Interviews> result = interviewRepo.findByInterview(30, LocalDate.now(), LocalTime.of(8, 0,0), LocalTime.of(9, 30,0), 31);

        // assert the results
        assertEquals(1, result.size());
        assertEquals(interview, result.get(0));
    }

    @Test
    public void testFindPastInterviewsByUser() {
        // create test data
        Interviews interview1 = new Interviews();
        interview1.setIntervieweeId(1);
        interview1.setInterviewerId(2);
        interview1.setDate(LocalDate.now().minusDays(1));
        interview1.setStartTime(LocalTime.of(9, 0));
        interview1.setEndTime(LocalTime.of(10, 0));
        interviewRepo.save(interview1);

        Interviews interview2 = new Interviews();
        interview2.setIntervieweeId(2);
        interview2.setInterviewerId(1);
        interview2.setDate(LocalDate.now().minusDays(2));
        interview2.setStartTime(LocalTime.of(11, 0));
        interview2.setEndTime(LocalTime.of(12, 0));
        interviewRepo.save(interview2);

        // call the method being tested
        List<Interviews> result = interviewRepo.findPastInterviewsByUser(1, LocalDate.now());

        // assert the results
        assertEquals(2, result.size());
    }

    @Test
    public void testFindUpcomingInterviewsByUser() {
        // create test data
        Interviews interview1 = new Interviews();
        interview1.setIntervieweeId(1);
        interview1.setInterviewerId(2);
        interview1.setDate(LocalDate.now().plusDays(1));
        interview1.setStartTime(LocalTime.of(9, 0));
        interview1.setEndTime(LocalTime.of(10, 0));
        interviewRepo.save(interview1);

        Interviews interview2 = new Interviews();
        interview2.setIntervieweeId(2);
        interview2.setInterviewerId(1);
        interview2.setDate(LocalDate.now().plusDays(2));
        interview2.setStartTime(LocalTime.of(11, 0));
        interview2.setEndTime(LocalTime.of(12, 0));
        interviewRepo.save(interview2);

        // call the method being tested
        List<Interviews> result = interviewRepo.findUpcomingInterviewsByUser(1, LocalDate.now());

        // assert the results
        assertEquals(2, result.size());
        assertEquals(interview1, result.get(0));
    }

    // Add more test cases for other methods as needed

}
