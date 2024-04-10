package com.Team12.HADBackEnd.security.services.smsService;

import org.springframework.stereotype.Service;


public interface SendSmsForFollowUp {

    boolean sendMessage(String language);
}
