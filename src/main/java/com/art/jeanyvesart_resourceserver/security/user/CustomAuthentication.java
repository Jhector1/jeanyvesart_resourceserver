package com.art.jeanyvesart_resourceserver.security.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

public class CustomAuthentication
        extends JwtAuthenticationToken {
    private final String priority;
    public CustomAuthentication(
            Jwt jwt,
            Collection<? extends GrantedAuthority> authorities,
            String priority) {

        super(jwt, authorities);
        this.priority = priority;
    }
    public String getPriority() {
        return priority;
    }
}


//package com.art.jeanyvesart_resourceserver.security.user;
//
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//
//import java.util.Collection;
//
//
//public class CustomAuthentication extends UsernamePasswordAuthenticationToken {
//
//    //private String customDetail; // Custom detail you want to associate
//    //private boolean authenticated=false;
//
//    public CustomAuthentication(Object principal, Object credentials) {
//        super(principal, credentials);
//
//    }
//
//    public CustomAuthentication(
//            Object principal,
//            Object credentials,
//            Collection<? extends GrantedAuthority> authorities) {
//        super(principal, credentials, authorities);
//
//    }
//
////    @Override
////    public Object getDetails() {
////        return userDetails;
////    }
//
//
//
//}


