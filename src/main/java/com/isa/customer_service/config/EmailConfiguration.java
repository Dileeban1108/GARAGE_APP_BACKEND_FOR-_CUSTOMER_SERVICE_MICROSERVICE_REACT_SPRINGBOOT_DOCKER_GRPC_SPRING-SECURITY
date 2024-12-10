package com.isa.customer_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfiguration {

    @Value("${spring.mail.username}")
    private String emailUsername;

    @Value("${spring.mail.password}")
    private String emailPassword;

    @Value("${spring.mail.host}")
    private String mailHost;

    @Value("${spring.mail.port}")
    private int mailPort;

    @Value("${spring.mail.protocol}")
    private String mailProtocol;

    @Value("${spring.mail.smtp.auth}")
    private boolean smtpAuth;

    @Value("${spring.mail.smtp.starttls.enable}")
    private boolean starttlsEnable;

    @Value("${spring.mail.default-encoding}")
    private String defaultEncoding;

    @Bean
    public JavaMailSenderImpl javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Set the basic mail properties
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);
        mailSender.setUsername(emailUsername);
        mailSender.setPassword(emailPassword);
        mailSender.setProtocol(mailProtocol);

        // Set JavaMail properties
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.starttls.enable", starttlsEnable);
        props.put("mail.smtp.ssl.trust", mailHost);  // Optional: for SSL
        props.put("mail.smtp.connectiontimeout", "10000"); // 10 seconds timeout
        props.put("mail.smtp.timeout", "10000");  // 10 seconds timeout
        props.put("mail.smtp.writetimeout", "10000");  // 10 seconds timeout
        props.put("mail.default-encoding", defaultEncoding);  // UTF-8 encoding

        return mailSender;
    }
}
