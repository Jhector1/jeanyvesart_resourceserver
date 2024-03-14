package com.art.jeanyvesart_resourceserver.security.csrf;

import com.art.jeanyvesart_resourceserver.security.service.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaTokenRepository
  extends JpaRepository<Token, Integer> {
 
  Optional<Token> findFirstByIdentifier(String identifier);

}