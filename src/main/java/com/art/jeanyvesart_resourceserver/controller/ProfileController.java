//package com.art.jeanyvesart_resourceserver.controller;
//
//import com.art.jeanyvesart_resourceserver.helper.Helper;
//import com.art.jeanyvesart_resourceserver.model.MyCustomer;
//import com.art.jeanyvesart_resourceserver.repository.CustomerRepository;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Optional;
//
//@ControllerAdvice
//@Controller
//@RequestMapping("/account")
//@SessionAttributes({"sessionId"})
//public class ProfileController {
//    private final CustomerRepository customerRepository;
//
//    public ProfileController(CustomerRepository repository) {
//        this.customerRepository = repository;
//    }
//
//    @GetMapping("/{sessionId}/profile")
//    public String getProfilePage(Model model, HttpServletResponse response){
//
//        Optional<MyCustomer> customer = customerRepository.findByEmail(Helper.getAuthenticatedEmail());
//       customer.ifPresent(myCustomer ->{
//           model.addAttribute("secret", myCustomer.getId());
//           model.addAttribute("fullName", (customer.get().getFullName()==null)?"Anonymous": customer.get().getFullName());
//       });
//
//
//
//        model.addAttribute( "sessionId" ,getSessionId());
//        Helper.setCookieValue("session_Id", getSessionId(), response);
//
////        System.out.println("details: "+a.getDetails());
//     // System.out.println("principal:  "+a.getPrincipal());
////        Cookie myCookie = new Cookie("my-session-id", sessionId);
////
////        // Set the cookie's max age (in seconds)
////        myCookie.setMaxAge(36000); // 1 hour (you can adjust this as needed)
////
////        // Set the cookie path (optional)
////        myCookie.setPath("/");
////
////        // Add the cookie to the response
////        response.addCookie(myCookie);
//
//        return "authenticated/userProfile";
//
//    }
//    @ModelAttribute
//    public static  String getSessionId(){
//       return Helper.getSessionId();
//    }
//
//}
