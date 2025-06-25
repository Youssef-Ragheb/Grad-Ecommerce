package com.grad.ecommerce_ai.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {


    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleEmail(String toMail, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            // Enable multipart for attachments
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toMail);
            helper.setSubject(subject);
            helper.setFrom("mario.sherif24@gmail.com");

            // HTML content with inline image
            String htmlContent = """
                <html>
                    <body>
                        <img src='cid:logoImage' style='width: 150px;'/>
                        <h2>%s</h2>
                        <p>%s</p>
                    </body>
                </html>
                """.formatted(subject, body);

            helper.setText(htmlContent, true);

            // Attach logo (from resources folder)
            ClassPathResource logo = new ClassPathResource("static/logo.png"); // Put logo.png in src/main/resources/static
            helper.addInline("logoImage", logo);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email with logo", e);
        }
    }
}
