package com.Team12.HADBackEnd.twilio;

import com.Team12.HADBackEnd.util.Constant;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.stereotype.Service;

@Service
public class TwilioMessageService {

    public static boolean sendSms(String smsNumber,String smsMessage){
        Twilio.init(Constant.ACCOUNT_SID, Constant.AUTH_TOKEN);
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("+919104031220"),// here I will use actual mobileNo
                        // to give actual number but now only supports one mobile number code for fetching actual number is above commented
                        new com.twilio.type.PhoneNumber("+12053509672"),
                        smsMessage)
                .create();
        System.out.println("message = " + message.getSid());

        return true;
//    public String sendSms(String smsNumber,String smsMessage){
//        Twilio.init(Constant.ACCOUNT_SID, Constant.AUTH_TOKEN);
//        Message message = Message.creator(
//                            new com.twilio.type.PhoneNumber("+919104031220"),// here I will use actual mobileNo
//                            // to give actual number but now only supports one mobile number code for fetching actual number is above commented
//                            new com.twilio.type.PhoneNumber("+12053509672"),
//                            smsMessage)
//                    .create();
//           System.out.println("message = " + message.getSid());
//
//        return message.getStatus().toString();
    }
}
