package com.Team12.HADBackEnd.util;

import com.Team12.HADBackEnd.models.*;
import org.apache.tomcat.util.bcel.Const;

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
//        if(citizen.getAge()!=0){
//              citizen.setAge(EncryptDecrypt.encrypt(citizen.getAge(),Constant.SECRET_KEY));
//        }
        if(citizen.getGender()!=null){
            citizen.setGender(EncryptDecrypt.encrypt(citizen.getGender(),Constant.SECRET_KEY));
        }
        if(citizen.getAddress()!=null)
        {
            citizen.setAddress(EncryptDecrypt.encrypt(citizen.getAddress(), Constant.SECRET_KEY));
        }
        if(citizen.getPincode()!=null){
            citizen.setPincode(EncryptDecrypt.encrypt(citizen.getPincode(),Constant.SECRET_KEY));
        }
        if(citizen.getState()!=null){
            citizen.setState(EncryptDecrypt.encrypt(citizen.getState(),Constant.SECRET_KEY));
        }
        if(citizen.getAbhaId()!=null){
            citizen.setAbhaId(EncryptDecrypt.encrypt(citizen.getAbhaId(),Constant.SECRET_KEY));
        }
    }
}
