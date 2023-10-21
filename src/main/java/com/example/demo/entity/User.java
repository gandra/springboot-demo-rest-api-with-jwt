package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
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

   @EqualsAndHashCode.Exclude
   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(
       name = "user_authority",
       joinColumns = @JoinColumn(name = "user_id"),
       inverseJoinColumns = @JoinColumn(name = "authority_id")
   )
   private Set<Authority> authorities;

}
