package com.Team12.HADBackEnd.security.PasswordENcoder;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class CustomPasswordEncoder implements PasswordEncoder {

    private static final int SALT_LENGTH = 16; // Length of salt in bytes
    private static final int ITERATIONS = 100000; // Number of iterations for hashing

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            // Generate a random salt
            byte[] salt = generateSalt();

            // Hash the password with salt using a strong hashing algorithm (e.g., SHA-512)
            byte[] hash = hashPassword(rawPassword.toString(), salt);

            // Combine salt and hash into a single string
            return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Error in encoding password", e);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            // Split the encoded password into salt and hash parts
            String[] parts = encodedPassword.split(":");
            if (parts.length != 2) {
                return false;
            }
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[1]);

            // Hash the provided password with the retrieved salt
            byte[] actualHash = hashPassword(rawPassword.toString(), salt);

            // Compare the expected hash with the actual hash
            return MessageDigest.isEqual(expectedHash, actualHash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Error in matching password", e);
        }
    }

    // Generate a random salt
    private byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private byte[] hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.reset();
        digest.update(salt);
        byte[] hashedBytes = digest.digest(password.getBytes());
        for (int i = 0; i < ITERATIONS; i++) {
            digest.reset();
            hashedBytes = digest.digest(hashedBytes);
        }
        return hashedBytes;
    }
}

