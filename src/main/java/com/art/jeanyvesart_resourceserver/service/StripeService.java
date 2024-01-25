package com.art.jeanyvesart_resourceserver.service;


import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {
    public Price createPrice(long amount, String currency, String name, String image, String productId, String customerId) throws StripeException {
        Map<String, String> extraData = new HashMap<>();
        extraData.put("artworkId", productId);
        extraData.put("customerId", customerId);
        ProductCreateParams productCreateParams = ProductCreateParams.builder()
                .setName(name)
                .setDescription("New")
                .addImage(image)
                .putAllMetadata(extraData)
                        .build();
        Product product = Product.create(productCreateParams);
        PriceCreateParams params = PriceCreateParams.builder()
                .setUnitAmount(amount)
                .setCurrency(currency).setProduct(product.getId())
                .build();
        return Price.create(params);
    }
}


