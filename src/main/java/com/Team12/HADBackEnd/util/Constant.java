package com.Team12.HADBackEnd.util;

import com.Team12.HADBackEnd.models.*;

public interface Constant {

    //----------------------Twilio Credentials----------------------------//
    String ACCOUNT_SID = "AC0518276df4ce7808b4641885bf208809";
    String AUTH_TOKEN = "8235aa6629a7ce0c5cd75bf58e2193c6";
    //--------------------------------------------------------------------//

    //--------------------Encrypt-Decrypt Secret Key----------------------//
    String SECRET_KEY = "8y/B?E(H+MbQeThWmZq4t6w9z$C&F)J@";
    //-------------------------------------------------------------------//


    //--------------Encrypting and Decrypting PII information------------------------//

    static void encryptPII(Citizen citizen) throws Exception{
        if(citizen.getId()!=null){
            citizen.setId(citizen.getId());
        }
        if(citizen.getName()!=null){
            citizen.setName(EncryptDecrypt.encrypt(citizen.getName(),Constant.SECRET_KEY));
        }
    }
}
