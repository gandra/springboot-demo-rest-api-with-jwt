package com.example.demo.security;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class MyBasicAuthenticationProvider implements AuthenticationProvider {

   private final UserDetailsService userDetailsService;
   private final PasswordEncoder passwordEncoder;

   // Omitted constructor

   @Override
   public Authentication authenticate(Authentication authentication) {
      String username = authentication.getName();
      String password = authentication.getCredentials().toString();

      UserDetails userDetails = userDetailsService.loadUserByUsername(username);

      if (passwordEncoder.matches(password, userDetails.getPassword())) {
         return new UsernamePasswordAuthenticationToken(
             userDetails,
             null,
             userDetails.getAuthorities());
      } else {
         throw new BadCredentialsException("Something went wrong!");
      }
   }

   @Override
   public boolean supports(Class<?> authenticationType) {
      return authenticationType.equals(UsernamePasswordAuthenticationToken.class);
   }

   // Omitted code
}