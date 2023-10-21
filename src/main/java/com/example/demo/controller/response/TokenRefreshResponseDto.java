package com.example.demo.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenRefreshResponseDto {
   private String accessToken;
   private String refreshToken;
   @Builder.Default
   private String tokenType = "Bearer";
}
