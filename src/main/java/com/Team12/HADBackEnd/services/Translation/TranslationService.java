package com.Team12.HADBackEnd.services.Translation;

import com.Team12.HADBackEnd.DTOs.Citizen.CitizenRegistrationDTO;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class TranslationService {

    // Example method to translate data to English based on the provided language
    public CitizenRegistrationDTO translateToEnglish(CitizenRegistrationDTO citizen) {
        String language = citizen.getLanguage();
        String name = citizen.getName();
        String age = String.valueOf(citizen.getAge());
        String translatedName = translateNameToEnglish(name, language);
        String translatedAge = translateNameToEnglish(age, language);
        String gender = citizen.getGender();
        String pincode = citizen.getPincode();
        String address = citizen.getAddress();
        String translatedAddress = translateNameToEnglish(address, language);
        String translatedPincode = translateNameToEnglish(pincode, language);
        String translatedGender = translateNameToEnglish(gender, language);
        int ageInt = Integer.parseInt(translatedAge);

        // Create a new CitizenRegistrationDTO with translated data
        CitizenRegistrationDTO translatedCitizen = new CitizenRegistrationDTO();
        translatedCitizen.setName(translatedName);
        translatedCitizen.setAge(ageInt);
        translatedCitizen.setGender(translatedGender);
        translatedCitizen.setPincode(translatedPincode);
        translatedCitizen.setAddress(translatedAddress);
        // Set other fields similarly

        return translatedCitizen;
    }

    // Example method to translate name to English based on the provided language
    private String translateNameToEnglish(String name, String language) {
        try {
            // Load credentials from the JSON file
            File credentialsPath = new File("E:\\IIIT BANGALORE\\Sem-2\\HAD\\Project\\HADBackEnd\\liquid-layout-420008-cf93cc74807e.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsPath));

            // Create a Translate instance with the loaded credentials
            Translate translate = TranslateOptions.newBuilder().setCredentials(credentials).build().getService();

            // Perform translation
            Translation translation = translate.translate(name,
                    Translate.TranslateOption.sourceLanguage(language),
                    Translate.TranslateOption.targetLanguage("en"));

            // Return translated text
            return translation.getTranslatedText();
        } catch (IOException e) {
            e.printStackTrace(); // Handle this appropriately, such as logging the error
            return name; // Return the original text in case of translation failure
        }
    }
}
