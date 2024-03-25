package com.zerobase.fintech.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ErrorResponse handleMyException(CustomException e){
      return new ErrorResponse(e.getErrorCode());
    }

}
