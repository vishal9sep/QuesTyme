package com.QuesTyme.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.QuesTyme.entity.RecurringMeeting;
import com.QuesTyme.repository.RecurringMeetingRepo;

@DataJpaTest
public class RecurringMeetingRepoTest {

    @Autowired
    private RecurringMeetingRepo recurringMeetingRepo;

    @Test
    public void testSave() {
        // create a recurring meeting
        RecurringMeeting recurringMeeting = new RecurringMeeting();
        recurringMeeting.setInstruction("Daily Stand-up Meeting");
        RecurringMeeting savedMeeting = recurringMeetingRepo.save(recurringMeeting);

        // assert that the meeting was saved
        assertNotNull(savedMeeting);
//        assertNotNull(savedMeeting.getId());
        assertEquals("Daily Stand-up Meeting", savedMeeting.getInstruction());
    }

    @Test
    public void testFindAll() {
        // create two recurring meetings
        RecurringMeeting recurringMeeting1 = new RecurringMeeting();
        recurringMeeting1.setInstruction("Daily Stand-up Meeting");
        recurringMeetingRepo.save(recurringMeeting1);

        RecurringMeeting recurringMeeting2 = new RecurringMeeting();
        recurringMeeting2.setInstruction("Weekly Team Meeting");
        recurringMeetingRepo.save(recurringMeeting2);

        // find all recurring meetings
        List<RecurringMeeting> recurringMeetings = recurringMeetingRepo.findAll();

        // assert that both meetings were found
        assertEquals(2, recurringMeetings.size());
    }

}
