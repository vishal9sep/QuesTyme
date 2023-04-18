package com.QuesTyme.controller;

import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.QuesTyme.service.ReminderService;

/**
*
 * Constructor for the ReminderController class.
 *
 * @param reminderService Service class for sending reminders.
*/

/**
 * Endpoint for sending reminders to users for events.
 *
 * @return ResponseEntity indicating success or failure of sending reminders.
 */

@RestController
@RequestMapping("/api/events")
public class ReminderController {

    private final ReminderService reminderService;

    @Autowired
    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @PostMapping("/reminder")
    public ResponseEntity<String> sendReminder() {
        try {
            reminderService.sendReminders();
            return ResponseEntity.ok("Reminder sent successfully");
        } catch (IllegalArgumentException | MessagingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error sending reminder: " + e.getMessage());
        }
    }

}