//package com.art.jeanyvesart_resourceserver.security.service;
//
//import com.art.jeanyvesart_resourceserver.service.MyOauth2UserService;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CustomOAuth2UserService extends DefaultOAuth2UserService {
//
//
//    private final MyOauth2UserService myOauth2UserService;
//
//
//    public CustomOAuth2UserService(MyOauth2UserService myOauth2UserService) {
//
//        this.myOauth2UserService = myOauth2UserService;
//    }
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//
//
//        // Process the user information and create an account if it doesn't exist
//        myOauth2UserService.processOAuth2User(oAuth2User);
//
//        return oAuth2User;
//    }
//
//
//}
//
