package com.example.demo.controller;

import com.example.demo.controller.request.LoginRequestDto;
import com.example.demo.controller.request.TokenRefreshRequestDto;
import com.example.demo.controller.response.JWTAuthResponseDto;
import com.example.demo.controller.response.TokenRefreshResponseDto;
import com.example.demo.entity.RefreshToken;
import com.example.demo.service.AuthService;
import com.example.demo.service.RefreshTokenService;
import com.example.demo.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

   private final AuthService authService;
   private final RefreshTokenService refreshTokenService;

   @PostMapping(value = {"login", "signin"})
   public ResponseEntity<JWTAuthResponseDto> login(@RequestBody LoginRequestDto loginDto) {
      JWTAuthResponseDto jwtAuthResponse = authService.login(loginDto);
      return ResponseEntity.ok(jwtAuthResponse);
   }

   @PostMapping("/refreshtoken")
   public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequestDto request) {
      String requestRefreshToken = request.getRefreshToken();
      TokenRefreshResponseDto tokenRefreshResponseDto = refreshTokenService.refreshToken(requestRefreshToken);
      return ResponseEntity.ok(tokenRefreshResponseDto);
   }
}
