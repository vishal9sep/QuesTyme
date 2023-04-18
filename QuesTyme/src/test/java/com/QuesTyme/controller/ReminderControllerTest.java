package com.QuesTyme.controller;

import java.util.concurrent.TimeUnit;
import javax.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.QuesTyme.service.ReminderService;

import static org.mockito.Mockito.*;


/**

 This is a JUnit test class for the ReminderController class.
 It tests the sendReminder() method of the ReminderController class using mock objects.
 It tests two scenarios:
 sendReminder() method should return "Reminder sent successfully" when the sendReminders() method of ReminderService
 completes successfully without throwing any exception.
 sendReminder() method should return "Error sending reminder: <error_message>" when the sendReminders() method of
 ReminderService throws a MessagingException.
 The tests use Mockito framework to mock ReminderService dependency and to verify that the sendReminders() method
 is called with the correct arguments.
 The tests also verify that the response from the sendReminder() method has the correct HTTP status code and message
 body.
 */


class ReminderControllerTest {

    @Mock
    private ReminderService reminderService;

    @InjectMocks
    private ReminderController reminderController;

    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testSendReminder() throws Exception {
        // setup
        setup();
        doNothing().when(reminderService).sendReminders();

        // test
        ResponseEntity<String> response = reminderController.sendReminder();

        // verify
        verify(reminderService).sendReminders();
        assert (response.getStatusCode() == HttpStatus.OK);
        assert (response.getBody().equals("Reminder sent successfully"));
    }

    @Test
    public void testSendReminderWithException() throws Exception {
        // setup
        setup();
        String errorMessage = "Error sending reminder";
        doThrow(new MessagingException(errorMessage)).when(reminderService).sendReminders();

        // test
        ResponseEntity<String> response = reminderController.sendReminder();

        // verify
        verify(reminderService).sendReminders();
        assert (response.getStatusCode() == HttpStatus.BAD_REQUEST);
        assert (response.getBody().equals("Error sending reminder: " + errorMessage));
    }
}
