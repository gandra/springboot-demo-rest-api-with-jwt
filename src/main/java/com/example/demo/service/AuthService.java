package com.example.demo.service;

import com.example.demo.controller.request.LoginRequestDto;
import com.example.demo.controller.response.JWTAuthResponseDto;
import com.example.demo.entity.RefreshToken;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.security.SecurityUser;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AuthService {
   private AuthenticationManager authenticationManager;
   private final JwtTokenProvider jwtTokenProvider;
   private final RefreshTokenService refreshTokenService;


   public JWTAuthResponseDto login(LoginRequestDto loginDto) {

      Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
          loginDto.getEmail(), loginDto.getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);

      String token = jwtTokenProvider.generateToken(authentication);

      SecurityUser principal = (SecurityUser)authentication.getPrincipal();
      RefreshToken refreshToken = refreshTokenService.createRefreshToken(principal.getId());

      return JWTAuthResponseDto.builder()
          .accessToken(token)
          .refreshToken(refreshToken.getToken())
          .roles(authentication.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList()))
          .build();
   }

}
