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

  private HttpStatus status;
  private String statusText;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
  private LocalDateTime timestamp;

  public ErrorDTO(HttpStatus status, String statusText) {
    this.timestamp = LocalDateTime.now();
    this.status = status;
    this.statusText = statusText;
  }
}
