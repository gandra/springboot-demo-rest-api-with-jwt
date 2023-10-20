package com.example.demo.seed;

import com.example.demo.entity.Authority;
import com.example.demo.entity.User;
import com.example.demo.repository.AuthorityRepository;
import com.example.demo.repository.UsersRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@AllArgsConstructor
@Component
public class AppDataSeeder implements ApplicationListener<ApplicationReadyEvent> {
   private final UsersRepository usersRepository;
   private final AuthorityRepository authorityRepository;
   private final PasswordEncoder passwordEncoder;

   @Override
   public void onApplicationEvent(ApplicationReadyEvent event) {
      log.info("APP STARTED!!!!!");

      seedUserData();
   }

   private void seedUserData() {

      if(usersRepository.count() > 0) {
         return;
      }

      Authority authorityUser = new Authority();
      authorityUser.setId(1);
      authorityUser.setName("ROLE_USER");
      authorityUser = authorityRepository.save(authorityUser);

      Authority authorityAdmin = new Authority();
      authorityAdmin.setId(2);
      authorityAdmin.setName("ROLE_ADMIN");
      authorityAdmin = authorityRepository.save(authorityAdmin);

      User userWithoutRoles = new User();
      userWithoutRoles.setEmail("user_without_roles@email.com");
      userWithoutRoles.setPassword(passwordEncoder.encode("12345"));
      usersRepository.save(userWithoutRoles);

      User user = new User();
      user.setEmail("user_user@email.com");
      user.setPassword(passwordEncoder.encode("12345"));
      user.setAuthorities(Set.of(authorityUser));
      authorityUser.setUsers(Set.of(user));
      usersRepository.save(user);

      User admin = new User();
      admin.setEmail("user_admin@email.com");
      admin.setPassword(passwordEncoder.encode("12345"));
      admin.setAuthorities(Set.of(authorityUser,authorityAdmin));
      authorityAdmin.setUsers(Set.of(admin));
      usersRepository.save(admin);
   }

}
