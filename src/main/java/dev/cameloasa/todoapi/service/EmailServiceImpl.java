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
  // Task notification email
  // ------------------------------
  public void sendTaskNotification(String email, TaskDTOView task, String actionType) {

    String subject;
    String html;

    switch (actionType.toLowerCase()) {
      case "created":
        subject = "A new task was created";
        html =
            "<p>Your task <strong>" + task.getTitle() + "</strong> was created successfully.</p>";
        break;

      case "updated":
        subject = "Your task was updated";
        html = "<p>Your task <strong>" + task.getTitle() + "</strong> was updated.</p>";
        break;

      case "deleted":
        subject = "Your task was deleted";
        html = "<p>Your task <strong>" + task.getTitle() + "</strong> was deleted.</p>";
        break;

      case "reassigned":
        subject = "Task reassigned";
        html =
            "<p>You have been assigned a new task: <strong>" + task.getTitle() + "</strong>.</p>";
        break;

      case "unassigned":
        subject = "Task unassigned";
        html =
            "<p>Your task <strong>"
                + task.getTitle()
                + "</strong> is no longer assigned to you.</p>";
        break;

      default:
        throw new IllegalArgumentException("Unknown action type: " + actionType);
    }

    EmailDTO dto = EmailDTO.builder().to(email).subject(subject).html(html).build();

    sendEmail(dto);
  }

  // ------------------------------
  // Password reset email
  // ------------------------------
  public void sendPasswordResetEmail(String email, String token) {

    String html;

    if (token == null) {
      // Admin reset — no link
      html =
          "<p>Your password has been reset by an administrator. "
              + "If you did not request this change, please contact support immediately.</p>";
    } else {
      // User reset — with link
      html =
          "<p>Click the link to reset your password: "
              + "<a href='https://yourapp/reset?token="
              + token
              + "'>Reset Password</a></p>";
    }

    EmailDTO dto =
        EmailDTO.builder().to(email).subject("Password reset request").html(html).build();

    sendEmail(dto);
  }
}
