//package com.art.jeanyvesart_resourceserver.security.service;
//
//import com.art.jeanyvesart_resourceserver.service.MyOauth2UserService;
//import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
//import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
//import org.springframework.security.oauth2.core.oidc.user.OidcUser;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CustomOicdUserService extends OidcUserService {
//
//
//    private final MyOauth2UserService myOauth2UserService;
//
//
//    public CustomOicdUserService( MyOauth2UserService myOauth2UserService) {
//
//        this.myOauth2UserService = myOauth2UserService;
//    }
//
//    @Override
//    public OidcUser loadUser(OidcUserRequest userRequest) {
//        OidcUser oAuth2User = super.loadUser(userRequest);
//
//        // Process the user information and create an account if it doesn't exist
//        myOauth2UserService.processOAuth2User( oAuth2User);
//
//        return oAuth2User;
//    }
//
//}
//
