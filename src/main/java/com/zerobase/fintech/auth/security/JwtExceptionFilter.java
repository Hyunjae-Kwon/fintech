package com.zerobase.fintech.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.zerobase.fintech.exception.ErrorCode;
import com.zerobase.fintech.exception.ErrorResponse;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (JwtException e) {
      String message = e.getMessage();
      if (message.equals(ErrorCode.JWT_TOKEN_WRONG_TYPE.getDescription())) {
        setResponse(response, ErrorCode.JWT_TOKEN_WRONG_TYPE);
      } else if (message.equals(ErrorCode.TOKEN_TIME_OUT.getDescription())) {
        setResponse(response, ErrorCode.TOKEN_TIME_OUT);
      }
    }
  }

  private void setResponse(HttpServletResponse response, ErrorCode errorCode)
      throws RuntimeException, IOException {
    ObjectMapper objectMapper = new JsonMapper();
    String responseJson = objectMapper.writeValueAsString(
        new ErrorResponse(errorCode));

    response.setContentType(
        MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
    response.setStatus(errorCode.getStatusCode());
    response.getWriter().print(responseJson);
  }

}
