package com.art.jeanyvesart_resourceserver.security.config;

import com.art.jeanyvesart_resourceserver.helper.Helper;
import com.art.jeanyvesart_resourceserver.security.component.authenticationProvider.CustomAuthenticationProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebAuthorizationConfig {

    private final CustomAuthenticationProvider authenticationProvider;

    public WebAuthorizationConfig(

            CustomAuthenticationProvider authenticationProvider) {

        this.authenticationProvider = authenticationProvider;

    }


    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {

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
        http.csrf(AbstractHttpConfigurer::disable).headers(h->h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        return http.build();
    }

}