package com.example.demo.controller;

import com.example.demo.controller.request.LoginRequestDto;
import com.example.demo.controller.response.JWTAuthResponseDto;
import com.example.demo.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {

   private AuthService authService;

   @PostMapping(value = {"login", "signin"})
   public ResponseEntity<JWTAuthResponseDto> login(@RequestBody LoginRequestDto loginDto) {
      JWTAuthResponseDto jwtAuthResponse = authService.login(loginDto);
      return ResponseEntity.ok(jwtAuthResponse);
   }
}
