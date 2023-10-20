package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeController {

   @GetMapping("hello")
   public String sayHello() {
      return "Hello from HomeController";
   }

   @GetMapping("/number/{code}")
   public String returnCodeNumber(@PathVariable String code) {
      return code;
   }

   @GetMapping("info")
   public String getInfo() {
      return "Info";
   }

   @GetMapping("info-admin")
   public String getInfoAdmin() {
      return "Info";
   }
}
