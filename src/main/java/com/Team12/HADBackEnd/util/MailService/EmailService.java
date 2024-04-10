package com.Team12.HADBackEnd.util.MailService;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendCredentialsByEmail(String email, String username, String password) throws MessagingException;
}
