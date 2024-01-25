//package com.art.jeanyvesart_resourceserver.service;//package com.art.jeanyvesart_resourceserver.service;
//
//import com.art.jeanyvesart_resourceserver.security.service.CustomOauth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//
//import org.springframework.security.oauth2.core.oidc.user.OidcUser;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//public class GenericOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
//
//    private final CustomOauth2UserService<OAuth2UserRequest, OidcUser> oidcUserService;
//    private final CustomOauth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService;
//
//    public GenericOAuth2UserService(
//            CustomOauth2UserService<OAuth2UserRequest, OidcUser> oidcUserService,
//            CustomOauth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService) {
//        this.oidcUserService = oidcUserService;
//        this.oauth2UserService = oauth2UserService;
//    }
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
//        if (isOidcUser(userRequest)) {
//            return oidcUserService.loadUser(userRequest);
//        } else {
//            return oauth2UserService.loadUser(userRequest);
//        }
//    }
//
//    private boolean isOidcUser(OAuth2UserRequest userRequest) {
//
//        // Determine whether the user request is for OIDC or not based on your logic
//        // You may check the client registration or other parameters
//
//        // Check if the client registration includes the userinfo_endpoint
//        boolean isOidc = userRequest.getAdditionalParameters().get("userinfo_endpoint") != null;
//
//        // You can add more checks based on your requirements
//
//        return isOidc;
//    }
//}
