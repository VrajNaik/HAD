package com.Team12.HADBackEnd.util.CredentialGenerator;

public interface CredentialService {

    String generateUniqueUsername(String role);

    String generateRandomPassword();
}
