package com.example.demo.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

@RestControllerAdvice
public class MyExceptionHandler {

   @ExceptionHandler(Exception.class)
   public ResponseEntity<ErrorMessageDto> handleGlobalException(Exception exception, WebRequest webRequest) {
      exception.printStackTrace();
      ErrorMessageDto exceptionDetails = ErrorMessageDto.builder()
          .timestamp(new Date())
          .message(exception.getMessage())
          .description(webRequest.getDescription(false))
          .build();
      return new ResponseEntity(exceptionDetails, HttpStatus.INTERNAL_SERVER_ERROR);
   }

   @ExceptionHandler(value = TokenRefreshException.class)
   @ResponseStatus(HttpStatus.FORBIDDEN)
   public ErrorMessageDto handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
      return ErrorMessageDto.builder()
          .statusCode(HttpStatus.FORBIDDEN.value())
          .timestamp(new Date())
          .message(ex.getMessage())
          .description(request.getDescription(false))
          .build();
   }

   @ExceptionHandler(TransactionSystemException.class)
   public ResponseEntity<ErrorMessageDto> handleJPAValidations(TransactionSystemException exception, WebRequest webRequest) {
      ErrorMessageDto exceptionDetails = ErrorMessageDto.builder()
          .timestamp(new Date())
          .message(exception.getMessage())
          .description(webRequest.getDescription(false))
          .build();

      if(exception.getCause().getCause() instanceof ConstraintViolationException) {
         ConstraintViolationException ve = (ConstraintViolationException) exception.getCause().getCause();

         Map<String, List<String>> errors = new HashMap<>();
         ve.getConstraintViolations().forEach(constraintViolation -> {
            String fieldName = constraintViolation.getPropertyPath().toString();
            String message = constraintViolation.getMessage();
            if(!errors.containsKey(fieldName)) {
               errors.put(fieldName, new ArrayList<>());
            }
            errors.get(fieldName).add(message);
         });

         exceptionDetails.setErrors(errors);
      }

      return new ResponseEntity(exceptionDetails, HttpStatus.BAD_REQUEST);
   }
}
