package com.example.movieappbackend.domain.service;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.movieappbackend.domain.exception.BusinessException;
import com.example.movieappbackend.domain.model.NotificationEmail;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {
	
	private final JavaMailSender mailSender;

	@Async
    void sendMail(NotificationEmail notificationEmail) {
    	MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("watchwiz01@gmail.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(notificationEmail.getBody());
        };
        try {
            mailSender.send(messagePreparator);
        } catch (MailException e) {
            log.error("Exception occurred when sending mail", e);
            throw new BusinessException("Exception occurred when sending mail to " + notificationEmail.getRecipient());
        }
    }
}
