package com.art.jeanyvesart_resourceserver.security.service;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Token {
 
  @Id

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
 
  private String identifier;
  private String token;

    public Token(String uuid, String identifier) {
      this.token = uuid; this.identifier = identifier;
    }

    // Omitted code
 
}