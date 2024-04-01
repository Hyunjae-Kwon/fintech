package com.zerobase.fintech.auth.security;

import static org.junit.jupiter.api.Assertions.*;

import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import java.util.Base64;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
@Transactional
@TestInstance(Lifecycle.PER_CLASS)
class TokenProviderTest {

  private final TokenProvider tokenProvider;
  private String secretKey;
  private String keyBase64Encoded;
  private SecretKey cachedSecretKey;

  TokenProviderTest(TokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  // getSecretKey() 테스트를 위한 TransferSecretKey() 결과값 주입
  @BeforeAll
  public void insertCachedSecretKey() {
    secretKey = "thiskeyisjwtsecretkeyforprovidethejwttoken";
    keyBase64Encoded = Base64.getEncoder().encodeToString(secretKey.getBytes());
    cachedSecretKey = Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
  }

  @Test
  @DisplayName("Get_SecretKey_Success")
  public void getSecretKeyTest() {
    assertEquals(tokenProvider.getSecretKey(), cachedSecretKey);
  }
}