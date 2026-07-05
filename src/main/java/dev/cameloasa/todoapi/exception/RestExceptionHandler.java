package dev.cameloasa.todoapi.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      @NonNull MethodArgumentNotValidException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {

    StringBuilder details = new StringBuilder();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(
            (fieldError) -> {
              details.append(fieldError.getField() + ": ");
              details.append(" ");
              details.append(fieldError.getDefaultMessage());
              details.append(", ");
            });

    ErrorDTO responseBody = new ErrorDTO(HttpStatus.BAD_REQUEST, details.toString());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
  }

  @ExceptionHandler({
    DataNotFoundException.class,
    DataDuplicateException.class,
    IllegalArgumentException.class
  })
  public ResponseEntity<ErrorDTO> handleCustomExceptions(Exception ex) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    if (ex instanceof DataNotFoundException) {
      status = HttpStatus.NOT_FOUND;
    }
    ErrorDTO responseBody = new ErrorDTO(status, ex.getMessage());
    return ResponseEntity.status(status).body(responseBody);
  }
}
