package com.Team12.HADBackEnd.services.BlackBox.smsService;

import com.Team12.HADBackEnd.twilio.TwilioMessageService;
import org.springframework.stereotype.Service;


@Service
public class SendSmsForFollowUpImpl implements SendSmsForFollowUp {

    @Override
    public boolean sendMessage(String language) {

        String msg;
        msg = "\n\nHello "
                + ", hope you are doing well. Your follow-up is scheduled for the date "
                + " of Health Record Id ";

        String mobileNumber="+919104031220";
        //SmsService.sendSms(mobileNumber,msg);

        //String language = "hi"; // Specify the language here (hi for Hindi)
        // Pass the message content without modifying it
        TwilioMessageService.sendSms(mobileNumber, getLocalizedMessage(msg, language));

        return true;

    }
    private String getLocalizedMessage(String message, String language) {
        // Handle language-specific messages here
        switch (language) {
            case "en":
                return message;
            case "mr":
                // Marathi message
                return message + "\n\nनमस्कार"
                        + ", आशा आहे की तुम्ही छान करत आहात. आपली फॉलो-अप तारीख "
                        + "आयडीच्या ";
            case "hi":
                // Hindi message
                return message + "\n\nनमस्ते"
                        + ", आपका स्वास्थ्य अवलोकन " // Example Hindi message
                        + "आयडीच्या की तारीख तक निर्धारित किया गया है। ";

            default:
                return message;
        }
    }
}
