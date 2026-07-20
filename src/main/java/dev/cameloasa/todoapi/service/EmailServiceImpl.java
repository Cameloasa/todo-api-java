package dev.cameloasa.todoapi.service;

import dev.cameloasa.todoapi.domanin.dto.EmailDTO;
import dev.cameloasa.todoapi.domanin.dto.TaskDTOView;
import dev.cameloasa.todoapi.exception.EmailServiceFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl {

  @Value("${email.fake:false}")
  private boolean fakeEmail;

  private final RestTemplate restTemplate = new RestTemplate();
  private final String EMAIL_SERVICE_SEND_URL = "http://localhost:9090/email";

  // ------------------------------
  // Generic email sender
  // ------------------------------
  public void sendEmail(EmailDTO dto) {

    if (fakeEmail) {
      System.out.println("FAKE EMAIL SENT TO: " + dto.getTo());
      return;
    }

    try {
      ResponseEntity<EmailDTO> response =
          restTemplate.exchange(
              EMAIL_SERVICE_SEND_URL, HttpMethod.POST, new HttpEntity<>(dto), EmailDTO.class);

      if (!response.getStatusCode().is2xxSuccessful()) {
        throw new EmailServiceFailedException(
            "Email service returned non-200 status: " + response.getStatusCode());
      }

    } catch (Exception ex) {
      throw new EmailServiceFailedException("Failed to send email", ex);
    }
  }

  // ------------------------------
  // Registration email
  // ------------------------------
  public void sendRegistrationEmail(String email) {
    EmailDTO dto =
        EmailDTO.builder()
            .to(email)
            .subject("Welcome to the TODO application")
            .html(
                "<p style='color: blue; font-size: 36px; text-align: center; background-color: lightgray; padding: 20px;'>Hello and welcome to our application. Please confirm your email.</p>")
            .build();

    sendEmail(dto);
  }

  // ------------------------------
  // Task created email
  // ------------------------------
  public void sendTaskCreatedEmail(String email, TaskDTOView task) {
    EmailDTO dto =
        EmailDTO.builder()
            .to(email)
            .subject("A new task was created")
            .html(
                "<p>Your task <strong>"
                    + task.getTitle()
                    + "</strong> was created successfully.</p>")
            .build();

    sendEmail(dto);
  }

  // ------------------------------
  // Password reset email
  // ------------------------------
  public void sendPasswordResetEmail(String email, String token) {
    EmailDTO dto =
        EmailDTO.builder()
            .to(email)
            .subject("Password reset request")
            .html(
                "<p>Click the link to reset your password: <a href='https://yourapp/reset?token="
                    + token
                    + "'>Reset Password</a></p>")
            .build();

    sendEmail(dto);
  }
}
