//package com.art.jeanyvesart_resourceserver.controller;
//
//import com.art.jeanyvesart_resourceserver.model.CustomerFavorite;
//import com.art.jeanyvesart_resourceserver.security.csrf.JpaTokenRepository;
//import com.art.jeanyvesart_resourceserver.security.service.Token;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/csrf")
//public class CSRF_Controller {
//    private final JpaTokenRepository jpaTokenRepository;
//
//    public CSRF_Controller(JpaTokenRepository jpaTokenRepository) {
//        this.jpaTokenRepository = jpaTokenRepository;
//    }
//
//    @GetMapping("/token/{identifier}")
//
//    public ResponseEntity<Token> getFavoriteProduct(@PathVariable("identifier") String identifier){
//        Optional<Token> optionalToken = jpaTokenRepository.findTokenByIdentifier(identifier);
//        return optionalToken.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NO_CONTENT));
//    }
//}
