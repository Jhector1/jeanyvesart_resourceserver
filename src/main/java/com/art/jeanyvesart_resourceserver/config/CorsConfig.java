package com.art.jeanyvesart_resourceserver.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Value("${base.url.client}")
    private String url;
    @Value("${base.url.server}")
    private String url2;
    private static String clientBaseUrl;
    private static String serverBaseUrl;

    @PostConstruct
    public void init() {
        clientBaseUrl = url;
        serverBaseUrl= url2;
    }
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Set allowed origins, methods, and headers
       // config.addAllowedOrigin("https://jeanyveshector.com/");
        config.addAllowedOrigin(clientBaseUrl);
        config.addAllowedOrigin("http://localhost:5050");
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOrigin("http://127.0.0.1:5050");



        //config.addAllowedOrigin(serverBaseUrl);


        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        //config.addAllowedOrigin("*");


        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
