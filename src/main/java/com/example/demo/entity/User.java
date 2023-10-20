package com.example.demo.entity;


import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "users")
public class User {

   @Id
   @SequenceGenerator(name = "USER_SEQ", sequenceName = "USER_SEQ", allocationSize=1)
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "USER_SEQ" )
   private Long id;

   @Column(unique = true)
   private String email;
   private String password;
   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(
       name = "user_authority",
       joinColumns = @JoinColumn(name = "user_id"),
       inverseJoinColumns = @JoinColumn(name = "authority_id")
   )
   private Set<Authority> authorities;


   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public Set<Authority> getAuthorities() {
      return authorities;
   }

   public void setAuthorities(Set<Authority> authorities) {
      this.authorities = authorities;
   }
}
