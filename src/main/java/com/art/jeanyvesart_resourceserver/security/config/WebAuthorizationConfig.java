package com.art.jeanyvesart_resourceserver.security.config;


import com.art.jeanyvesart_resourceserver.security.csrf.CustomCsrfTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
public class WebAuthorizationConfig {
    @Value("${keySetURI}")
    private String keySetUri;
    private final JwtAuthenticationConverter converter;

    private final CustomCsrfTokenRepository csrfTokenRepository;
//    private final CustomAuthenticationProvider authenticationProvider;


    public WebAuthorizationConfig(

            JwtAuthenticationConverter converter, CustomCsrfTokenRepository csrfTokenRepository){//, CustomAuthenticationProvider authenticationProvider) {
        this.converter = converter;
        this.csrfTokenRepository = csrfTokenRepository;

//        this.authenticationProvider = authenticationProvider;

    }


    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf(c -> {
            c.csrfTokenRepository(csrfTokenRepository);
            c.csrfTokenRequestHandler(
                    new CsrfTokenRequestAttributeHandler()

            );
            c.ignoringRequestMatchers("/h2-console/**", "/cart/**", "/favorite/**", "/stripe/**", "/api/artworks/purchase/**");
        }).headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        http.oauth2ResourceServer(
                c -> c.jwt(
                        j -> j.jwkSetUri(keySetUri)
                                .jwtAuthenticationConverter(converter)
                )
        );
        http.authorizeHttpRequests(
                c -> {

                    c.requestMatchers("/account/**")
                            .authenticated();
                    c.anyRequest()
                            .permitAll();

                });

//
//        http.formLogin(c ->
//                c.loginPage("/login")
//                        .defaultSuccessUrl("/account/" + Helper.getSessionId() + "/profile", true)
//                        .permitAll()
//        )
//             http.oauth2Login(o ->
//                o.loginPage("/login")
//                        .userInfoEndpoint(u -> u
//                                .oidcUserService(oicdUserService)
//                                .userService(oAuth2UserService))
//
//                        .defaultSuccessUrl("/account/" + Helper.getSessionId() + "/profile", true)
//                        .permitAll()
//        );
//                     .logout(l -> l
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/login")
//                .permitAll());

       // http.authenticationProvider(authenticationProvider);
        http.csrf(A->A.disable()).headers(h->h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        return http.build();
    }

}