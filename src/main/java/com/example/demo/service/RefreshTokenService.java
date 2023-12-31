package com.example.demo.service;

import com.example.demo.controller.response.TokenRefreshResponseDto;
import com.example.demo.entity.RefreshToken;
import com.example.demo.exception.TokenRefreshException;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.repository.UsersRepository;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
   @Value("${app.jwt.expiration-refresh-minutes}")
   private Long refreshTokenDurationMinutes;

   @Value("${app.jwt.secret}")
   private String jwtSecret;

   @Autowired
   private RefreshTokenRepository refreshTokenRepository;

   @Autowired
   private UsersRepository userRepository;

   public Optional<RefreshToken> findByToken(String token) {
      return refreshTokenRepository.findByToken(token);
   }

   public RefreshToken createRefreshToken(Long userId) {

      RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId).orElse(new RefreshToken());
//      RefreshToken refreshToken = new RefreshToken();

      refreshToken.setUser(userRepository.findById(userId).get());
      refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMinutes * 60 * 1000));
      refreshToken.setToken(UUID.randomUUID().toString());

      refreshToken = refreshTokenRepository.save(refreshToken);
      return refreshToken;
   }

   public RefreshToken verifyExpiration(RefreshToken token) {
      if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
         refreshTokenRepository.delete(token);
         throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
      }

      return token;
   }

   @Transactional
   public int deleteByUserId(Long userId) {
      return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
   }

   public TokenRefreshResponseDto refreshToken(String requestRefreshToken) {
      return findByToken(requestRefreshToken)
          .map(rf -> verifyExpiration(rf))
          .map(RefreshToken::getUser)
          .map(user -> {
             String token = JwtUtil.generateJwtToken(user, refreshTokenDurationMinutes, jwtSecret);
             return TokenRefreshResponseDto.builder().accessToken(token).refreshToken(requestRefreshToken).build();
          })
          .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
   }
}
