package com.Team12.HADBackEnd.services.Common;

import com.Team12.HADBackEnd.models.User;


public interface ForgotPasswordService {

    void initiatePasswordReset(String email);

    void sendResetPasswordEmail(User user);

    boolean resetPassword(String token, String newPassword);

    boolean changePassword(String username, String newPassword);
}
