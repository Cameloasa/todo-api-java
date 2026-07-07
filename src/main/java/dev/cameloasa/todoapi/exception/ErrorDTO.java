package dev.cameloasa.todoapi.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO {

  private int statusCode; // ex: 400
  private String status; // ex: BAD_REQUEST
  private String message; // ex: "password: must contain uppercase"
  private String path; // ex: "/users"
  private String method; // ex: "POST"

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime timestamp;

  public ErrorDTO(HttpStatus status, String message, String path, String method) {
    this.statusCode = status.value();
    this.status = status.name();
    this.message = message;
    this.path = path;
    this.method = method;
    this.timestamp = LocalDateTime.now();
  }
}
