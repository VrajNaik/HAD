package com.Team12.HADBackEnd.util;

import java.util.Random;

public interface Constant {

    //---------------------------Roles---------------------------------//
    String FIELD_HEALTH_WORKER = "FHW";
    //-----------------------------------------------------------------//

    //----------------------Twilio Credentials----------------------------//
    String ACCOUNT_SID = "AC0518276df4ce7808b4641885bf208809";
    String AUTH_TOKEN = "60038aaa23541d0d53ea6ce3a336b11c";
    //--------------------------------------------------------------------//

    //---------------------Numbers-----------------------------------//
    Integer OTP_LENGTH = 6;
    //---------------------------------------------------------------//

    // Generating OTPs
    static String generateOtp() {
        String numbers = "0123456789";
        String otp = "";
        for(int i = 0; i < OTP_LENGTH; i++)
            otp += numbers.charAt(new Random().nextInt(numbers.length()));
        return otp;
    }
}
