package com.Team12.HADBackEnd.util.MailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendCredentialsByEmail(String email, String username, String password) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setTo(email);
        helper.setSubject("Welcome to Zencare - Your Credentials");

        String emailBody = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Zencare - Your Credentials</title>\n" +
                "</head>\n" +
                "<body style=\"font-family: Arial, sans-serif;\">\n" +
                "    <div style=\"background-color: #f5f5f5; padding: 20px; border-radius: 10px;\">\n" +
                "        <h1 style=\"color: #333333;\">Welcome to Zencare!</h1>\n" +
                "        <p style=\"color: #666666;\">Below are your login credentials:</p>\n" +
                "        <ul>\n" +
                "            <li><strong>Username:</strong> " + username + "</li>\n" +
                "            <li><strong>Password:</strong> " + password + "</li>\n" +
                "        </ul>\n" +
                "        <p style=\"color: #666666;\">Please keep your credentials secure and do not share them with anyone.</p>\n" +
                "        <p style=\"color: #666666;\">If you have any questions or need assistance, feel free to contact our support team.</p>\n" +
                "        <p style=\"color: #666666;\">Best regards,<br>Zencare Team</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
        helper.setText(emailBody, true);
        helper.setFrom("noreply@zencare.com");
        javaMailSender.send(mimeMessage);
    }
}
