package com.Team12.HADBackEnd.services.Translation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/translate")
public class TranslationController {

    private static final Logger logger = LoggerFactory.getLogger(TranslationController.class);

    @Autowired
    private TranslationServiceSample translationService;

    @GetMapping("/toHindi")
    public ResponseEntity<String> translateTextToHindi(@RequestParam String text) {
        logger.debug("Request to translate text to Hindi: {}", text);
        String translatedText = translationService.translateToHindi(text);
        logger.debug("Translated text: {}", translatedText);
        return ResponseEntity.ok(translatedText);
    }
}
