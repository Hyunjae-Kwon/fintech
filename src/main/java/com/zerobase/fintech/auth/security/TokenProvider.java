package com.zerobase.fintech.auth.security;

import com.zerobase.fintech.exception.ErrorCode;
import com.zerobase.fintech.user.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

  private final UserService userService;

  @Value("${spring.jwt.secret}")
  private String secretKey;

  private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60;// 1시간

  // 시크릿 키를 담는 변수
  private SecretKey cachedSecretKey;

  // plain -> 시크릿 키 변환 method
  private SecretKey TransferSecretKey() {
    String keyBase64Encoded = Base64.getEncoder()
        .encodeToString(secretKey.getBytes());
    return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
  }

  // 시크릿 키를 반환하는 method
  public SecretKey getSecretKey() {
    if (cachedSecretKey == null) {
      cachedSecretKey = TransferSecretKey();
    }

    return cachedSecretKey;
  }

  // 토큰 생성
  public String generateToken(String userId) {
    SecretKey key = getSecretKey();

    Claims claims = Jwts.claims().setSubject(userId);

    var now = new Date();
    var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now) // 토큰 생성 시간
        .setExpiration(expiredDate) // 토큰 만료 시간
        .signWith(key, SignatureAlgorithm.HS256)  // 사용할 암호화 알고리즘, 인코딩된 시크릿 키
        .compact();
  }

  // 토큰으로 userId 찾기
  public String getUserId(String token) {
    return this.parseClaims(token).getSubject();
  }

  // 토큰 유효성 검사
  public boolean validateToken(String token) {
    try {
      if (!StringUtils.hasText(token)) {
        return false;
      }
      Claims claims = this.parseClaims(token);
      return !claims.getExpiration().before(new Date());
    } catch (JwtException e) {
      throw new JwtException(e.getMessage());
    }
  }

  private Claims parseClaims(String token) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(TransferSecretKey())
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (ExpiredJwtException e) {
      throw new JwtException(ErrorCode.TOKEN_TIME_OUT.getDescription());
    } catch (SignatureException e) {
      throw new JwtException(ErrorCode.JWT_TOKEN_WRONG_TYPE.getDescription());
    }
  }

  public Authentication getAuthentication(String token) {
    String userId = this.getUserId(token);
    UserDetails userDetails = userService.loadUserByUsername(userId);
    return new UsernamePasswordAuthenticationToken(
        userDetails, "", userDetails.getAuthorities());
  }

}
