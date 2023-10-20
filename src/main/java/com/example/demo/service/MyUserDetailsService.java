package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UsersRepository;
import com.example.demo.security.SecurityUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {
   private final UsersRepository usersRepository;

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      User userFound = usersRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
      return new SecurityUser(userFound);
   }
}
