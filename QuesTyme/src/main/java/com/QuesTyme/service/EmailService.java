package com.QuesTyme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailService {
	 @Autowired
	    private JavaMailSender javaMailSender;

	    public void sendEmail(String toEmail, String subject, String body) throws MailException {

	        SimpleMailMessage mailMessage = new SimpleMailMessage();
	        mailMessage.setTo(toEmail);
	        mailMessage.setSubject(subject);
	        mailMessage.setText(body);
	        javaMailSender.send(mailMessage);

	    }
}
