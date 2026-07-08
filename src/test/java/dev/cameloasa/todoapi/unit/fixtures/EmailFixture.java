package dev.cameloasa.todoapi.unit.fixtures;

import dev.cameloasa.todoapi.domanin.dto.EmailDTO;

public class EmailFixture {
    
    public static EmailDTO sampleEmailDTO() {
        return EmailDTO.builder()
                .to("test@example.com")
                .subject("Welcome to the TODO application")
                .html("<p>Hello, this is a test email.</p>")
                .build();
    }

    public static EmailDTO sampleRegistrationEmail(String to) {
        return EmailDTO.builder()
                .to(to)
                .subject("Welcome, you are now registered to the TODO application")
                .html("<p style='color: blue; font-size: 36px; text-align: center; background-color: lightgray; padding: 20px;'>Hello and welcome to our application. Please confirm your email.</p>")
                .build();
    }

    public static EmailDTO sampleCustomEmail(String to, String subject, String html) {
        return EmailDTO.builder()
                .to(to)
                .subject(subject)
                .html(html)
                .build();
    }
}
