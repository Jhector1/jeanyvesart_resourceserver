package com.art.jeanyvesart_resourceserver.controller;

import com.art.jeanyvesart_resourceserver.component.MyResourceManager;
import com.art.jeanyvesart_resourceserver.dto.ProductList;
import com.art.jeanyvesart_resourceserver.dto.StripeProduct;
import com.art.jeanyvesart_resourceserver.helper.Shippable;
import com.art.jeanyvesart_resourceserver.model.Checkout;
import com.art.jeanyvesart_resourceserver.model.MyProduct;
import com.art.jeanyvesart_resourceserver.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@SessionAttributes({"checkout"})
@RestController

@RequestMapping("/cart/checkout")
public class CheckoutController {
    private final StripeService stripeService;
    @Value("${base.url.client}")
    private String baseUrl;

    @Value("${stripe.secret.key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public CheckoutController(StripeService stripeService) {
        this.stripeService = stripeService;
    }


    @ModelAttribute(name = "checkout")
    public Checkout checkout() {
        return new Checkout();
    }


    @PostMapping("/create-checkout-session")
    public ResponseEntity<?> createCheckoutSession(@RequestBody ProductList productList) {





        try {
            List<SessionCreateParams.LineItem> allCartData = new ArrayList<>();
            for (StripeProduct stripeProduct : productList.getCartProductList()) {
                MyProduct buyerCart = stripeProduct.getMyProduct();
                allCartData.add(SessionCreateParams.LineItem.builder()
                        .setPrice(stripeService.createPrice(
                                        Long.parseLong(
                                                buyerCart.getPrice()) * 100L,
                                        "USD",
                                        buyerCart.getTitle(),
                                       "https://jeanyveshector.com"+ buyerCart.getImageUrl(),
                                        String.valueOf(buyerCart.getId()),
                                        productList.getCustomerId())
                                .getId())
                        .setQuantity((long) stripeProduct.getQuantity())
                        .build());
            }
            SessionCreateParams params = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(baseUrl + "/cart/artworks/checkout/success?session_id={CHECKOUT_SESSION_ID}")

                    .setCancelUrl(baseUrl + "/cart").setShippingAddressCollection(SessionCreateParams
                            .ShippingAddressCollection
                            .builder()
                            .addAllAllowedCountry(Shippable
                                    .displayAllowedCountries())
                            .build())
//
//                    .addShippingOption(
//                            SessionCreateParams.ShippingOption.builder()
//                                    .setShippingRateData(
//                                            SessionCreateParams.ShippingOption.ShippingRateData.builder()
//                                                    .setType(
//                                                            SessionCreateParams.ShippingOption.ShippingRateData.Type.FIXED_AMOUNT
//                                                    )
//                                                    .setFixedAmount(
//                                                            SessionCreateParams.ShippingOption.ShippingRateData.FixedAmount.builder()
//                                                                    .setAmount(0L)
//                                                                    .setCurrency("usd")
//                                                                    .build()
//                                                    )
//                                                    .setDisplayName("Free shipping")
//                                                    .setDeliveryEstimate(
//                                                            SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.builder()
//                                                                    .setMinimum(
//                                                                            SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Minimum.builder()
//                                                                                    .setUnit(
//                                                                                            SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Minimum.Unit.BUSINESS_DAY
//                                                                                    )
//                                                                                    .setValue(5L)
//                                                                                    .build()
//                                                                    )
//                                                                    .setMaximum(
//                                                                            SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Maximum.builder()
//                                                                                    .setUnit(
//                                                                                            SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Maximum.Unit.BUSINESS_DAY
//                                                                                    )
//                                                                                    .setValue(7L)
//                                                                                    .build()
//                                                                    )
//                                                                    .build()
//                                                    )
//                                                    .build()
//                                    )
//                                    .build()
//                    )
//                    .addShippingOption(
//                            SessionCreateParams.ShippingOption.builder()
//                                    .setShippingRateData(
//                                            SessionCreateParams.ShippingOption.ShippingRateData.builder()
//                                                    .setType(
//                                                            SessionCreateParams.ShippingOption.ShippingRateData.Type.FIXED_AMOUNT
//                                                    )
//                                                    .setFixedAmount(
//                                                            SessionCreateParams.ShippingOption.ShippingRateData.FixedAmount.builder()
//                                                                    .setAmount(1500L)
//                                                                    .setCurrency("usd")
//                                                                    .build()
//                                                    )
//                                                    .setDisplayName("Next day air")
//                                                    .setDeliveryEstimate(
//                                                            SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.builder()
//                                                                    .setMinimum(
//                                                                            SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Minimum.builder()
//                                                                                    .setUnit(
//                                                                                            SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Minimum.Unit.BUSINESS_DAY
//                                                                                    )
//                                                                                    .setValue(1L)
//                                                                                    .build()
//                                                                    )
//                                                                    .setMaximum(
//                                                                            SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Maximum.builder()
//                                                                                    .setUnit(
//                                                                                            SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Maximum.Unit.BUSINESS_DAY
//                                                                                    )
//                                                                                    .setValue(1L)
//                                                                                    .build()
//                                                                    )
//                                                                    .build()
//                                                    )
//                                                    .build()
//                                    )
//                                    .build()
//                    )
                    .setConsentCollection(
                            SessionCreateParams.ConsentCollection
                                    .builder()
                                    .setTermsOfService(SessionCreateParams.ConsentCollection.TermsOfService.REQUIRED)
                                    .build()
                    ).setAutomaticTax(SessionCreateParams
                            .AutomaticTax.builder()
                            .setEnabled(true)
                            .build()).addAllLineItem(allCartData)

                    .build();
            Session session = Session.create(params);
//            Gson gson = new GsonBuilder().create();
//            String sessionJson = gson.toJson(session);
//            JsonObject sessionJsonObject = JsonParser.parseString(sessionJson).getAsJsonObject();
//            String sessionId = sessionJsonObject.get("id").getAsString();
            return new ResponseEntity<>(session.getId(), HttpStatus.OK);
        } catch (StripeException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
//
////    @Bean
////    public WebMvcConfigurer corsConfigurer() {
////        return new WebMvcConfigurer() {
////            @Override
////            public void addCorsMappings(CorsRegistry registry) {
////
////                registry.addMapping("/**")
////                        .allowedOrigins("*")
////                        .allowedMethods("*")
////                        .allowedHeaders("*");
////                //.allowCredentials(true);
////            }
////        };
////    }
}
