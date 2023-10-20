package com.example.demo.config;

import com.example.demo.security.JWTTokenValidatorFilter;
import com.example.demo.security.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(
    // securedEnabled = true,
    // jsr250Enabled = true,
    prePostEnabled = true)
public class SecurityConfig {

   @Value("${app.jwt.secret}")
   private String jwtSecret;

   @Autowired
   private JwtAuthenticationEntryPoint authenticationEntryPoint;

   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
      return configuration.getAuthenticationManager();
   }

   @Bean
   SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

      http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

      http.csrf(csrf -> csrf.disable());
//      http.csrf(csrf -> csrf.ignoringRequestMatchers("/public/**", "/api/**"));
//      http.cors(c -> {
//         CorsConfigurationSource source = request -> {
//            CorsConfiguration config = new CorsConfiguration();
//            config.setAllowedOrigins(List.of("example.com"));
//            config.setAllowedMethods(List.of("*"));
//            config.setAllowedHeaders(List.of("*"));
//            config.setAllowCredentials(true);
//            config.setExposedHeaders(List.of(SecurityConstants.JWT_HEADER));
//            return config;
//         };
//         c.configurationSource(source);
//      });

      http
          .addFilterBefore(new JWTTokenValidatorFilter(jwtSecret), BasicAuthenticationFilter.class)
          // TODO understand securityMatcher() usage
//          .securityMatcher("/api/**")
          .exceptionHandling(c -> c.authenticationEntryPoint(authenticationEntryPoint))
          .authorizeHttpRequests(
              c -> c
                  .requestMatchers(HttpMethod.GET, "/api/hello").permitAll()
                  .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                  .requestMatchers(HttpMethod.POST, "/api/signin").permitAll()
                  .requestMatchers("/api/number/{code:^[0-9]*$}").permitAll()
                  .requestMatchers("/api/info/**").hasAnyRole("USER", "ADMIN")
                  .requestMatchers("/api/info-admin/**").hasRole("ADMIN")
                  .anyRequest().authenticated()
//                  .anyRequest().hasAnyRole("USER", "ADMIN")
//                  .authenticated()
          );

      return http.build();
   }


//   @Bean
//   public PasswordEncoder passwordEncoder() {
//      Map<String, PasswordEncoder> encoders = new HashMap<>();
//
//      encoders.put("noop", NoOpPasswordEncoder.getInstance());
//      encoders.put("bcrypt", new BCryptPasswordEncoder());
//
//      return new DelegatingPasswordEncoder("bcrypt", encoders);
//   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

}
