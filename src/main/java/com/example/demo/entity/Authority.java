package com.example.demo.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "authorities")
public class Authority {
   @Id
   private Integer id;

   @Column(unique = true)
   private String name;

   @ManyToMany(mappedBy="authorities")
   private Set<User> users;

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }


   public void setUsers(Set<User> users) {
      this.users = users;
   }
}
