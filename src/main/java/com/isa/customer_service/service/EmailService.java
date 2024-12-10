package com.isa.customer_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.MailException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;  // Correct import for MessagingException
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendVerificationEmail(String to, String subject, String text) {
        try {
            logger.debug("Preparing to send email to: {}", to);
            logger.debug("Subject: {}", subject);
            logger.debug("Sending email to {}", to);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);  // Setting the text to be HTML (true)

            logger.debug("Sending email...");
            javaMailSender.send(message);
            logger.debug("Email sent successfully to: {}", to);
        } catch (MessagingException e) {  // Catch MessagingException explicitly
            logger.error("Error with message format or transport to " + to, e);
            throw new EmailSendException("Failed to send email to " + to, e);
        } catch (MailException e) {  // Catch MailException for SMTP issues
            logger.error("Error sending email to " + to, e);
            throw new EmailSendException("Failed to send email to " + to, e);
        }
    }

    public static class EmailSendException extends RuntimeException {
        public EmailSendException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
