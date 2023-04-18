package com.QuesTyme.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.QuesTyme.entity.Interviews;
import com.QuesTyme.entity.User;
import com.QuesTyme.repository.InterviewRepo;
import com.QuesTyme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class ReminderService {

    @Autowired
    private final JavaMailSender emailSender;

    @Autowired
    private final InterviewRepo interviewRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public ReminderService(JavaMailSender emailSender, InterviewRepo eventRepository) {
        this.emailSender = emailSender;
        this.interviewRepo = eventRepository;
    }


    /**
     *
     * @throws MessagingException
     * The "sendReminders" method is annotated with "@Scheduled" which makes it run periodically. This method fetches all the scheduled interviews from the database using "findAll" method of InterviewRepo.
     * It then iterates through the scheduled interviews and checks if a reminder has already been sent for each interview. If not, it adds the interviewee ID to a list of interviewees that require a reminder.
     *
     * It then fetches all the users whose IDs are in the list using "findByIds" method of UserRepository. For each user, it creates a MimeMessage object and sets the recipient, sender, subject, and email content using the MimeMessageHelper.
     * The email content is an HTML formatted string that includes details of the scheduled interview and a Zoom link for the meeting.
     *
     * After sending the email, the reminderSent flag of the interview is set to false, and the interview is saved to the database using the "save" method of InterviewRepo.
     *
     * This method throws a "MessagingException" in case there is an error while sending the email.
     *
     * Overall, this class provides a useful service for automatically sending email reminders to interviewees before their scheduled interviews.
     */


    //@Scheduled(fixedRate = 1800000)
    @Scheduled(cron = "0 */15 * * * ?")
    public void sendReminders() throws MessagingException {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderTime = now.plusMinutes(30);

        List<Interviews> scheduledInterviews = this.interviewRepo.findAll().stream()
                .filter(interview -> interview.getStartTime().isAfter(LocalTime.from(now)) && interview.getStartTime().isBefore(LocalTime.from(reminderTime)))
                .collect(Collectors.toList());

        List<Integer> list = new ArrayList<>();

        for (Interviews scheduledInterview : scheduledInterviews) {
            if (!scheduledInterview.isReminderSent()) {
                list.add(scheduledInterview.getIntervieweeId());
            }

            if (!list.isEmpty()) {
            List<User> users = userRepository.findByIds(list);
            for (User recipient : users) {

                MimeMessage message = emailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(recipient.getEmail());
                helper.setFrom("noreply@masaischool.com");
                helper.setSubject("Remider to join meeting");
                String content =
                        "<div style='background-color: #EAF1F2;'>"
                                + "<h2 style='font-family: Arial, Helvetica, sans-serif; text-align: center;padding-top: 3%;margin-bottom: 2%;'>Masai School | Course</h2>"
                                + "<div style='background-color:	#FFFFFF;  margin-right: 20%;margin-left: 20%; margin-bottom:0;'>"
                                + "<h2 style='font-family: Arial, Helvetica, sans-serif; margin-left:5%; padding-top:4%; margin-bottom:4%;font-size: large; '>Hello!</h2>"
                                + "<p style='font-size: 15px;font-family: Arial, Helvetica, sans-serif; color:#909090; margin-top: 4%; margin-right: 5%;margin-bottom: 4%;margin-left: 5%;'>This mail is a reminder to join meeting on <a href=\"" + scheduledInterview.getDate() + "\">" + scheduledInterview.getDate() + "</a> at <a href=\"" + scheduledInterview.getStartTime() + "\">" + scheduledInterview.getStartTime() + "</a></p>"
                                + "<button style=\"display:block; height:40px;margin:auto; background-color:black; border-radius: 7px; color:white;\"> <a href=\"" + scheduledInterview.getMeetingLink() + "\"   style=\"display: inline-block; background-color: black; color: white; padding: 10px 20px; text-align: center; text-decoration: none;\">zoom link</a></button>"
                                + "<p style='font-size: 15px; font-family: Arial, Helvetica, sans-serif; color:#909090; margin-top: 4%; margin-right: 5%;margin-bottom: 4%;margin-left: 5%;'>please join meeting at time.</p>"
                                + ""
                                + ""
                                + "<p style=' font-size: 15px;font-family: Arial, Helvetica, sans-serif; color:#909090; margin-top: 4%; margin-right: 5%;margin-bottom: 4%;margin-left: 5%;'>Regards,<br>Masai School | Course</p>"
                                + ""
                                + "<hr style='border:none; border-top: 1px solid #ccc; margin-top: 20px; margin-bottom: 20px; margin-left:5%;margin-right:5%;'>"
                                + "<p style=' font-size: 13px;font-family: Arial, Helvetica, sans-serif; color:#909090;  margin-top: 4%; margin-right: 5%;margin-bottom: 4%;margin-left: 5%; padding-bottom:4%;'>\r\n"
                                + "If you're having trouble clicking the \"Link button\" button, copy and paste the URL below into your web browser: <a href=\"" + scheduledInterview.getMeetingLink() + "\">" + scheduledInterview.getMeetingLink() + "</a></p>"
                                + "</div>"
                                + "<div style='padding-bottom:15px;'>"
                                + "<p style='font-family: Arial, Helvetica, sans-serif; color:#909090; text-align: center;' >Â© 2023 Masai School | Course. All rights reserved.</p>"
                                + "</div>"
                                + "</div>";

                helper.setText(content, true);
                emailSender.send(message);
            }
        }
            scheduledInterview.setReminderSent(true);
            interviewRepo.save(scheduledInterview);
        }
    }

}