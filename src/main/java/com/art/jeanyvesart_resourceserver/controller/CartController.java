//package art.jeanyvesart_resourceserver.controller;
//
//import com.art.jeanyvesart_resourceserver.component.EndPointPool;
//import com.art.jeanyvesart_resourceserver.helper.Helper;
//import com.art.jeanyvesart_resourceserver.model.*;
//import com.art.jeanyvesart_resourceserver.repository.CustomerRepository;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Controller
//@RequestMapping(path = "/cart", produces = "application/json")
//@SessionAttributes({"stripePublicKey"})
//
//public class CartController {
//    private final CustomerRepository customerRepository;
//    @Value("${stripe.public.key}")
//    private String stripePublicKey;
//
//    public CartController(CustomerRepository customerRepository) {
//        this.customerRepository = customerRepository;
//    }
//
//
//    @GetMapping
//    public String getCartPage(Model model, HttpServletRequest request) {
////        Cookie[] cookies = request.getCookies();
////
////        if (cookies != null) {
////            // Iterate through the cookies
////            for (Cookie cookie : cookies) {
////                System.out.println(cookie.getName() + " " + cookie.getValue());
////            }
////        }
//        //System.out.println(Helper.getCookieValue(request, "user12345"));
//        String userId = Helper.getCookieValue(request, "user12345");
//        if (userId != null) {
//            Optional<MyCustomer> customer = customerRepository.findById(Objects.requireNonNull(userId));
//            if (customer.isPresent()) {
//                CustomerFavorite favoriteArtwork = customer.get().getCustomerFavorite();
//                if (favoriteArtwork != null) {
//                    List<MyProduct> products = favoriteArtwork.getCustomerDataHelpers().stream().map(CustomerDataHelper::getMyProduct).collect(Collectors.toList());
//                    model.addAttribute("allArtParent", "allArtParent");
//
//                    model.addAttribute("favoriteProducts", products);
//                }
//            }
//        }
//        model.addAttribute("stripePublicKey", stripePublicKey);
//        model.addAttribute("userCartEndpoint", EndPointPool.getMap().get("userCartEndpoint"));
//
//        return "cart_page";
//    }
//
//    @ModelAttribute("stripePublicKey")
//    public String getStripePublicKey() {
//
//        return stripePublicKey;
//    }
//
//
//    @GetMapping("/artworks/checkout/success")
//    public String successPage(@RequestParam String session_id, Model model) {
//        model.addAttribute("session_id", session_id);
//
//        return "success_page";
//    }
//
//    @GetMapping("/artworks/checkout/cancel")
//    public String cancelPage(@RequestParam String session_id, Model model) {
//        model.addAttribute("session_id", session_id);
//
//        return "error_page";
//    }
//
//}
////
