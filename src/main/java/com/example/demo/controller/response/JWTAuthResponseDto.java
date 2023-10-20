package com.example.demo.controller.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JWTAuthResponseDto {
    private String accessToken;
    private String refreshToken;
    private List<String> roles;
    private String tokenType = "Bearer";
}
