package com.Team12.HADBackEnd.util;

public interface Constant {

    //----------------------Twilio Credentials----------------------------//
    String ACCOUNT_SID = "AC0518276df4ce7808b4641885bf208809";
    String AUTH_TOKEN = "60038aaa23541d0d53ea6ce3a336b11c";
    //--------------------------------------------------------------------//

    //---------------------Numbers-----------------------------------//
    Integer DAY = 1000*60*60*24; // milliseconds
    Integer OTP_LENGTH = 6;
    Integer OTP_EXPIRATION_TIME = 1000 * 60 * 30;
    //---------------------------------------------------------------//

    //---------------------FollowUps Status---------------------------//
    String FOLLOW_UP_PENDING = "Assigned";
    String FOLLOW_UP_DONE = "Completed";
    //----------------------------------------------------------------//
}
