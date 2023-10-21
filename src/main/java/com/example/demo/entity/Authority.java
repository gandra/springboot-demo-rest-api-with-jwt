package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;

@Data
@Entity
@Table(name = "authorities")
public class Authority {
   @Id
   private Integer id;

   @Column(unique = true)
   private String name;

   @EqualsAndHashCode.Exclude
   @ToString.Exclude
   @JsonIgnore
   @ManyToMany(mappedBy="authorities")
   private Set<User> users;

}
