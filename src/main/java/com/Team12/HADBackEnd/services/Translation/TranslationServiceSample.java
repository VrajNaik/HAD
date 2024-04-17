package com.Team12.HADBackEnd.services.Translation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class TranslationServiceSample {

    private static final Logger logger = LoggerFactory.getLogger(TranslationService.class);
    private final String apiUrl = "https://libretranslate.de/translate";

    public String translateToHindi(String originalText) {
        RestTemplate restTemplate = new RestTemplate();

        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Prepare request body
        Map<String, Object> map = new HashMap<>();
        map.put("q", originalText);
        map.put("source", "en");
        map.put("target", "hi");
        map.put("format", "text");

        // Create the HTTP entity
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        logger.info("Sending request to LibreTranslate API for translation: {}", originalText);

        // Make the request
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

        logger.info("Received response from LibreTranslate API: {}", response.getBody());

        return response.getBody(); // You may need to parse JSON depending on how you handle the response
    }
}
