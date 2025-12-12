package com.art.jeanyvesart_resourceserver.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class TurnstileService {

    private final RestClient client;
    private final String secret;

    public TurnstileService(@Value("${turnstile.secret}") String secret, RestClient.Builder builder) {
        this.secret = secret;
        this.client = builder.baseUrl("https://challenges.cloudflare.com").build();
    }

    public boolean verify(String token, String remoteIp) {
        if (token == null || token.isBlank()) return false;

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("secret", secret);
        form.add("response", token);
        if (remoteIp != null && !remoteIp.isBlank()) form.add("remoteip", remoteIp);

        TurnstileResponse resp = client.post()
                .uri("/turnstile/v0/siteverify")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(TurnstileResponse.class);

        return resp != null && resp.success();
    }

    public record TurnstileResponse(
            boolean success,
            String hostname,
            String action,
            String cdata,
            String[] error_codes
    ) {}
}
