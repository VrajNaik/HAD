package com.Team12.HADBackEnd.services.BlackBox.smsService;

import com.Team12.HADBackEnd.models.Citizen;
import com.Team12.HADBackEnd.models.FieldHealthCareWorker;
import com.Team12.HADBackEnd.models.FollowUp;
import com.Team12.HADBackEnd.models.HealthRecord;
import com.Team12.HADBackEnd.repository.FollowUpRepository;
import com.Team12.HADBackEnd.twilio.TwilioMessageService;
import com.Team12.HADBackEnd.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import java.util.Date;
import java.sql.Date;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class SendSmsForFollowUpImpl implements SendSmsForFollowUp {

    private static final Logger logger = LoggerFactory.getLogger(SendSmsForFollowUpImpl.class);


    @Autowired
    FollowUpRepository followUpRepository;

    @Override
    public boolean sendMessage(String language) {

        logger.info("sendMessage method called with language: {}", language);

        //Set<Citizen> citizenSet = new HashSet<>();
        ArrayList<FollowUp> followUps = followUpRepository.findByStatus(Constant.FOLLOW_UP_PENDING);
        if (followUps.size() == 0) {
            logger.info("No follow-ups found");
            return true;
        }

        try {
            logger.info("Entered in try block");

            Map<FieldHealthCareWorker, StringBuilder> messageMap = new HashMap<>();

            followUps.forEach(followUp -> {
                if (followUp.getHealthRecord().getFieldHealthCareWorker() != null) {
                    FieldHealthCareWorker fieldHealthWorker = followUp.getHealthRecord().getFieldHealthCareWorker();
                    StringBuilder messageBuilder = messageMap.getOrDefault(fieldHealthWorker, new StringBuilder());
                    Citizen citizen = followUp.getHealthRecord().getCitizen();
                    // HealthRecord healthRecord = followUp.getHealthRecord();
                    java.util.Date date = followUp.getDate();

                    // Check if the greeting has been sent for this healthcare worker
                    boolean greetingSent = messageBuilder.length() > 0;

                   // FieldHealthCareWorker fieldHealthWorker = followUp.getHealthRecord().getFieldHealthCareWorker();

                    String msg;
                    try {

//                        msg = (greetingSent ? "" : "\n\nHello " + fieldHealthWorker.getName() + ", hope you are doing well.")
//                                + ", hope you are doing well. Your follow-up is scheduled for the date: " + date
//                                + " for the citizen: " + citizen.getName() + " with abha Id: " + citizen.getAbhaId() + " with adress : " + citizen.getAddress();

                        msg = (greetingSent ? "\n" : "") // Add line break if not first message
                                + (greetingSent ? "" : "Hello " + fieldHealthWorker.getName() + ", hope you are doing well.\n")
                                + "Your follow-up is scheduled for the date: " + date
                                + " for the citizen: " + citizen.getName() + " with abha Id: " + citizen.getAbhaId() + " with adress : " + citizen.getAddress();

                        messageBuilder.append(msg);
                        messageMap.put(fieldHealthWorker, messageBuilder);




                        //SmsService.sendSms(mobileNumber,msg);
                        //String language = "hi"; // Specify the language here (hi for Hindi)
                        // Pass the message content without modifying it
                       // TwilioMessageService.sendSms(mobileNumber, getLocalizedMessage(msg, language));
                        logger.info("SMS sent successfully for follow-up with citizen: {}", citizen.getName());
                    } catch (Exception e) {
                        logger.error("Error sending SMS for follow-up with citizen: {}", citizen.getName(), e);
                        throw new RuntimeException(e);
                    }
                }
            });

            messageMap.forEach((healthWorker, messageBuilder) -> {
                try {
                    String mobileNumber = "+919104031220";
                    //String mobileNumber = healthWorker.getMobileNumber(); // Assuming each healthcare worker has a mobile number
                    TwilioMessageService.sendSms(mobileNumber, getLocalizedMessage(messageBuilder.toString(), language));
                    logger.info("SMS sent successfully for follow-up with health worker: {}", healthWorker.getName());
                } catch (Exception e) {
                    logger.error("Error sending SMS for follow-up with health worker: {}", healthWorker.getName(), e);
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            logger.error("An error occurred during SMS sending: {}", e.getMessage(), e);
            System.out.println("e = " + e);
            return false;
        }
        logger.info("sendMessage method completed successfully");
        return true;
    }

    private String getLocalizedMessage(String message, String language) {
        // Handle language-specific messages here
        switch (language) {
            case "en":
                return message;
            case "mr":
                // Marathi message
                return message + "\n\nनमस्कार"
                        + ", आशा आहे की तुम्ही छान करत आहात. आपली फॉलो-अप तारीख "
                        + "आयडीच्या ";
            case "hi":
                // Hindi message
                return message + "\n\nनमस्ते"
                        + ", आपका स्वास्थ्य अवलोकन " // Example Hindi message
                        + "आयडीच्या की तारीख तक निर्धारित किया गया है। ";

            default:
                return message;
        }
    }
}
