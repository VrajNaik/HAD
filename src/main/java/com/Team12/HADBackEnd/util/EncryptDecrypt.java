package com.Team12.HADBackEnd.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public interface EncryptDecrypt {

//    String ALGORITHM = "AES/ECB/PKCS5Padding";
//
//    static String encrypt(String plainText, String keyString) throws Exception {
//        byte[] key = keyString.getBytes(StandardCharsets.UTF_8);
//        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
//
//        Cipher cipher = Cipher.getInstance(ALGORITHM);
//        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
//
//        byte[] encrypted = cipher.doFinal(plainText.getBytes());
//        return Base64.getEncoder().encodeToString(encrypted);
//    }
//
//    static String decrypt(String encryptedText, String keyString) throws Exception {
//        byte[] key = keyString.getBytes(StandardCharsets.UTF_8);
//        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
//
//        Cipher cipher = Cipher.getInstance(ALGORITHM);
//        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
//
//        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
//        return new String(decrypted, StandardCharsets.UTF_8);
//    }

    String ALGORITHM = "AES/CBC/PKCS5Padding";

    static String encrypt(String plainText, String keyString) throws Exception {
        byte[] key = keyString.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecureRandom randomSecureRandom = new SecureRandom();
        byte[] ivBytes = new byte[cipher.getBlockSize()];
        randomSecureRandom.nextBytes(ivBytes);
        IvParameterSpec ivParams = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParams);

        byte[] encrypted = cipher.doFinal(plainText.getBytes());
        byte[] encryptedIVandText = new byte[ivBytes.length + encrypted.length];
        System.arraycopy(ivBytes, 0, encryptedIVandText, 0, ivBytes.length);
        System.arraycopy(encrypted, 0, encryptedIVandText, ivBytes.length, encrypted.length);
        return Base64.getEncoder().encodeToString(encryptedIVandText);
    }

    static String decrypt(String encryptedText, String keyString) throws Exception {
        byte[] key = keyString.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

        byte[] decoded = Base64.getDecoder().decode(encryptedText);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        byte[] ivBytes = new byte[cipher.getBlockSize()];
        System.arraycopy(decoded, 0, ivBytes, 0, ivBytes.length);
        IvParameterSpec ivParams = new IvParameterSpec(ivBytes);

        byte[] actualEncryptedData = new byte[decoded.length - ivBytes.length];
        System.arraycopy(decoded, ivBytes.length, actualEncryptedData, 0, actualEncryptedData.length);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParams);

        byte[] decrypted = cipher.doFinal(actualEncryptedData);
        return new String(decrypted, StandardCharsets.UTF_8);
    }
}
