package com.watchman.metaserver.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler(ResponseStatusException.class)
  public String handleResponseStatusException(ResponseStatusException ex) {
    return ex.getReason();
  }
}
