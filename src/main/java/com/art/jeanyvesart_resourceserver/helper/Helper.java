package com.art.jeanyvesart_resourceserver.helper;

import com.art.jeanyvesart_resourceserver.repository.CustomerRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.UUID;

public class Helper {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public Helper(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            // Iterate through the cookies
            for (Cookie cookie : cookies) {
                if (cookie.getName().trim().equals(cookieName)) {
                    // Found the desired cookie
                    return cookie.getValue();
                }
            }
        }

        // Cookie not found
        return null;
    }

    public static String getAuthenticatedEmail(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication a = context.getAuthentication();
        String email;
        if(a.getPrincipal() instanceof OAuth2User){
            OAuth2User oAuth2User = (OAuth2User) a.getPrincipal();
            email = checkEmail(oAuth2User);
        }
        else{
            email = a.getPrincipal().toString();
        }
        return email;


    }
    public static String checkEmail(OAuth2User oAuth2User) {

        String email = oAuth2User.getAttribute("email");


        if (email == null) {
            String id = oAuth2User.getAttribute("id");
            String name = oAuth2User.getAttribute("name");

            return name.replace(" ", "") + id + "@jeanyveshector.com";
        } else {
            return email;
        }
    }


    public static String getSessionId(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication a = context.getAuthentication();
        String sessionId ;
        if(a==null){
            sessionId = UUID.randomUUID().toString();
        }
        else {
            WebAuthenticationDetails details = (WebAuthenticationDetails) a.getDetails();

            // Access the session ID
            sessionId = details.getSessionId();


        } return sessionId;
    }
    public static void setCookieValue(String cookiename, String cookievalue,HttpServletResponse response) {
        // Create a cookie
        Cookie cookie = new Cookie(cookiename, cookievalue);

        // Optional: Set additional attributes for the cookie
        // cookie.setMaxAge(3600); // Set the cookie's maximum age in seconds (1 hour in this example)
        cookie.setPath("/"); // Set the path for which the cookie is valid (root path in this example)

        // Add the cookie to the response
        response.addCookie(cookie);


        // Other response logic, if needed
    }
}
