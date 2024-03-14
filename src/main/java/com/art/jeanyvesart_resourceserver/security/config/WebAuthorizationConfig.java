package com.art.jeanyvesart_resourceserver.security.config;

import com.art.jeanyvesart_resourceserver.security.component.authenticationProvider.CustomAuthenticationProvider;

import com.art.jeanyvesart_resourceserver.security.csrf.CustomCsrfTokenRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
public class WebAuthorizationConfig {
    private final CustomCsrfTokenRepository csrfTokenRepository;
    private final CustomAuthenticationProvider authenticationProvider;


    public WebAuthorizationConfig(

            CustomCsrfTokenRepository csrfTokenRepository, CustomAuthenticationProvider authenticationProvider) {
        this.csrfTokenRepository = csrfTokenRepository;

        this.authenticationProvider = authenticationProvider;

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

        http.authenticationProvider(authenticationProvider);
        //http.csrf(AbstractHttpConfigurer::disable);///.headers(h->h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        return http.build();
    }

}