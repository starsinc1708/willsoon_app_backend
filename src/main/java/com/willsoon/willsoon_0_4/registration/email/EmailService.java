package com.willsoon.willsoon_0_4.registration.email;

import com.willsoon.willsoon_0_4.security.config.EmailConfig;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender {

    private final static Logger LOGGER = LoggerFactory
            .getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    private final EmailConfig emailConfig;

    @Override
    @Async
    public void send(String to, String email) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, "utf-8");
            helper.setFrom(emailConfig.getUsername());
            helper.setTo(to);
            helper.setText(email, true);
            helper.setSubject("Confirm You Email | WillSoon");
            mailSender.send(message);
        } catch (MessagingException e) {
            LOGGER.error("failed to send email", e);
            throw new MailSendException("failed to send email");
        }
    }
}
