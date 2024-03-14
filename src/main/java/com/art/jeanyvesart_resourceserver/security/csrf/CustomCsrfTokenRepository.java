package com.art.jeanyvesart_resourceserver.security.csrf;

import com.art.jeanyvesart_resourceserver.security.service.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.security.web.csrf.DeferredCsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(produces = "application/json")
@Slf4j
public class CustomCsrfTokenRepository implements CsrfTokenRepository {
    RestTemplate restTemplate = new RestTemplate();
    private final JpaTokenRepository jpaTokenRepository;

    public CustomCsrfTokenRepository(JpaTokenRepository jpaTokenRepository) {
        this.jpaTokenRepository = jpaTokenRepository;
    }

    // Omitted constructor


    @Override
    public void saveToken(
            CsrfToken csrfToken,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        String identifier =
                httpServletResponse.getHeader("X-IDENTIFIER");
        log.info("identifier save, {}", identifier);
        if (identifier == null) {
            identifier = httpServletRequest.getHeader("X-IDENTIFIER");
        }
        Optional<Token> existingToken =
                jpaTokenRepository.findFirstByIdentifier(identifier);

        if (existingToken.isPresent()) {
            Token token = existingToken.get();
            System.out.println(csrfToken.getToken());
            token.setToken(csrfToken.getToken());
        } else {
            if (identifier != null) {
                Token token = new Token();
                token.setToken(csrfToken.getToken());
                token.setIdentifier(identifier);
                jpaTokenRepository.save(token);
            }
        }
    }

    @Override
    @GetMapping("/csrf/token")
    public CsrfToken generateToken(HttpServletRequest httpServletRequest) {
        String uuid = UUID.randomUUID().toString();
//        jpaTokenRepository.save(new Token(uuid));
//        log.info("token generate, {}", uuid);
        String identifier = httpServletRequest.getHeader("X-IDENTIFIER");
        log.info("identifier save, {}", identifier);
//        if (identifier == null) {
//            identifier = httpServletRequest.getHeader("X-IDENTIFIER");
//        }
        Optional<Token> existingToken =
                jpaTokenRepository.findFirstByIdentifier(identifier);

        if (existingToken.isPresent()) {
            Token token = existingToken.get();

            return new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", token.getToken());

        } else {
            if (identifier != null) {
                Token token = new Token(uuid, identifier);

                //token.setIdentifier(identifier);
                jpaTokenRepository.save(token);
            }
            return new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", uuid);

        }

    }

    @Override


    public CsrfToken loadToken(
            HttpServletRequest httpServletRequest) {

        String identifier = (String) httpServletRequest.getAttribute("X-IDENTIFIER");
        if (identifier == null) {
            identifier = httpServletRequest.getHeader("X-IDENTIFIER");
        }

        log.info("identifier, {}", identifier);
        Optional<Token> existingToken =
                jpaTokenRepository
                        .findFirstByIdentifier(identifier);

        if (existingToken.isPresent()) {


            Token token = existingToken.get();
            log.info("token, {}", token);
            return new DefaultCsrfToken(
                    "X-CSRF-TOKEN",
                    "_csrf",
                    token.getToken());
        }

        return null;
    }

    //  private HttpHeaders getHeadersFromOtherEndpoint() {
//    // Call another endpoint within the same application
//
//    ResponseEntity<String> response = restTemplate.getForEntity("/otherendpoint", String.class);
//    return response.getHeaders();
//  }
    @Override
    public DeferredCsrfToken loadDeferredToken(HttpServletRequest request, HttpServletResponse response) {
        return CsrfTokenRepository.super.loadDeferredToken(request, response);
    }
}