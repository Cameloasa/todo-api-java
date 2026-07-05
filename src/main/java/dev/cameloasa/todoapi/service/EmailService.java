package dev.cameloasa.todoapi.service;

import dev.cameloasa.todoapi.domanin.dto.EmailDTO;
import dev.cameloasa.todoapi.exception.EmailServiceFailedException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {

  private final RestTemplate restTemplate = new RestTemplate();
  private final String EMAIL_SERVICE_SEND_URL = "http://localhost:9090/email";

  public HttpStatusCode sendRegistrationEmail(String registeredEmail) {

    EmailDTO dto =
        EmailDTO.builder()
            .to(registeredEmail)
            .subject("Welcome, you are now registered to the TODO application")
            .html(
                "<p style='color: blue; font-size: 36px; text-align: center; background-color: lightgray; padding: 20px;'>Hello and welcome to our application. Please confirm your email.</p>")
            .build();

    try {
      ResponseEntity<EmailDTO> responseEntity = sendEmail(dto);

      if (!responseEntity.getStatusCode().is2xxSuccessful()) {
        throw new EmailServiceFailedException(
            "Email service returned non-200 status: " + responseEntity.getStatusCode());
      }

      return responseEntity.getStatusCode();

    } catch (Exception ex) {
      throw new EmailServiceFailedException("Failed to send registration email", ex);
    }
  }

  @SuppressWarnings("null")
  public ResponseEntity<EmailDTO> sendEmail(EmailDTO emailDTO) {
    return restTemplate.exchange(
        EMAIL_SERVICE_SEND_URL, HttpMethod.POST, new HttpEntity<>(emailDTO), EmailDTO.class);
  }
}
