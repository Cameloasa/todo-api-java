package dev.cameloasa.todoapi.exception;

import jakarta.servlet.http.HttpServletRequest;


import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  // ---------------------------------------------------------
  // 1. Bean Validation (DTO validation) → 400 Bad Request
  // ---------------------------------------------------------
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      @NonNull MethodArgumentNotValidException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {

    String message =
        ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining("; "));

    HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();

    ErrorDTO body =
        new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            message,
            servletRequest.getRequestURI(),
            servletRequest.getMethod());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  // ---------------------------------------------------------
  //  400 Bad Request (IllegalArgumentException)
  // ---------------------------------------------------------
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorDTO> handleIllegalArgument(
      IllegalArgumentException ex, WebRequest request) {

    HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();

    ErrorDTO body =
        new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ex.getMessage(),
            servletRequest.getRequestURI(),
            servletRequest.getMethod());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  // ---------------------------------------------------------
  //  401 Unauthorized
  // ---------------------------------------------------------
  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<ErrorDTO> handleInvalidCredentials(
      InvalidCredentialsException ex, WebRequest request) {

    HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();

    ErrorDTO body =
        new ErrorDTO(
            HttpStatus.UNAUTHORIZED,
            ex.getMessage(),
            servletRequest.getRequestURI(),
            servletRequest.getMethod());

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
  }

  // ---------------------------------------------------------
//  403 Forbidden
// ---------------------------------------------------------
@ExceptionHandler(AccessDeniedException.class)
public ResponseEntity<ErrorDTO> handleAccessDenied(
    AccessDeniedException ex, WebRequest request) {

  HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();

  ErrorDTO body =
      new ErrorDTO(
          HttpStatus.FORBIDDEN,
          "Access denied: insufficient permissions",
          servletRequest.getRequestURI(),
          servletRequest.getMethod());

  return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
}


  // ---------------------------------------------------------
  //  404 Not Found
  // ---------------------------------------------------------
  @ExceptionHandler(DataNotFoundException.class)
  public ResponseEntity<ErrorDTO> handleNotFound(DataNotFoundException ex, WebRequest request) {

    HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();

    ErrorDTO body =
        new ErrorDTO(
            HttpStatus.NOT_FOUND,
            ex.getMessage(),
            servletRequest.getRequestURI(),
            servletRequest.getMethod());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  // ---------------------------------------------------------
  //  409 Conflict
  // ---------------------------------------------------------
  @ExceptionHandler(DataDuplicateException.class)
  public ResponseEntity<ErrorDTO> handleDuplicate(DataDuplicateException ex, WebRequest request) {

    HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();

    ErrorDTO body =
        new ErrorDTO(
            HttpStatus.CONFLICT,
            ex.getMessage(),
            servletRequest.getRequestURI(),
            servletRequest.getMethod());

    return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
  }

  // ---------------------------------------------------------
  //  503 Email service failed exception
  // ---------------------------------------------------------
  @ExceptionHandler(EmailServiceFailedException.class)
  public ResponseEntity<ErrorDTO> handleEmailFailure(
      EmailServiceFailedException ex, WebRequest request) {

    HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();

    ErrorDTO body =
        new ErrorDTO(
            HttpStatus.SERVICE_UNAVAILABLE,
            ex.getMessage(),
            servletRequest.getRequestURI(),
            servletRequest.getMethod());

    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
  }

  // ---------------------------------------------------------
  //  500 Internal Server Error (fallback)
  // ---------------------------------------------------------
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDTO> handleGeneral(Exception ex, WebRequest request) {

    HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();

    ErrorDTO body =
        new ErrorDTO(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Unexpected error: " + ex.getMessage(),
            servletRequest.getRequestURI(),
            servletRequest.getMethod());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }

  
}
