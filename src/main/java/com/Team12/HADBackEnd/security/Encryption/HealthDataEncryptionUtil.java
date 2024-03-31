package com.Team12.HADBackEnd.security.Encryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.Base64;

public class HealthDataEncryptionUtil {
    private static final String ENCRYPTION_ALGORITHM = "PBEWithMD5AndDES";
    private static final int ITERATION_COUNT = 65536;
    private static final byte[] SALT = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };

    public static String encrypt(String healthData, String password) throws Exception {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ENCRYPTION_ALGORITHM);
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), SALT, ITERATION_COUNT);
        SecretKey secretKey = keyFactory.generateSecret(keySpec);
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, ITERATION_COUNT);

        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);

        byte[] encryptedData = cipher.doFinal(healthData.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    public static String decrypt(String encryptedHealthData, String password) throws Exception {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ENCRYPTION_ALGORITHM);
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), SALT, ITERATION_COUNT);
        SecretKey secretKey = keyFactory.generateSecret(keySpec);
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, ITERATION_COUNT);

        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);

        byte[] decodedEncryptedData = Base64.getDecoder().decode(encryptedHealthData);
        byte[] decryptedData = cipher.doFinal(decodedEncryptedData);
        return new String(decryptedData);
    }
}

