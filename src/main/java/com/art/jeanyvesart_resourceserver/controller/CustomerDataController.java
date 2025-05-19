package com.art.jeanyvesart_resourceserver.controller;

import com.art.jeanyvesart_resourceserver.dto.CustomerProduct;
import com.art.jeanyvesart_resourceserver.helper.ProductAndInventory;
import com.art.jeanyvesart_resourceserver.model.*;
import com.art.jeanyvesart_resourceserver.repository.*;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Abstract controller for managing customer data related to products and inventories.
 *
 * @param <H> The type of helper associated with customer data.
 * @param <R> The type of repository for managing customer data.
 * @param <M> The type of model representing customer data.
 */
public abstract class CustomerDataController<H extends CustomerDataHelper, R extends CustomerDataRepository<H, M>, M extends CustomerData<H>> {

    private final R repository;
    private final InventoryRepository inventoryRepository;
    private final CustomerDataHelperRepository<H> customerDataHelperRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    /**
     * Constructs a CustomerDataController with the specified repositories.
     *
     * @param repository                    The repository for managing customer data.
     * @param productRepository             The repository for managing products.
     * @param customerRepository            The repository for managing customers.
     * @param inventoryRepository           The repository for managing inventory.
     * @param customerDataHelperRepository  The repository for managing customer data helpers.
     */
    public CustomerDataController(R repository, ProductRepository productRepository, CustomerRepository customerRepository,
                                  InventoryRepository inventoryRepository, CustomerDataHelperRepository<H> customerDataHelperRepository) {
        this.repository = repository;
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.customerDataHelperRepository = customerDataHelperRepository;
    }

    /**
     * Retrieves a list of product IDs for a given user.
     *
     * @param userID The ID of the user.
     * @return A ResponseEntity containing a list of product IDs or NO_CONTENT if no data is found.
     */
    @GetMapping("/product_id/{userID}")
    public ResponseEntity<List<Long>> getUserDataByUserId(@PathVariable("userID") String userID) {
        Optional<M> items = repository.findByMyCustomer_Id(userID);

        if (items.isPresent()) {
            List<H> products = items.get().getCustomerDataHelpers();
            List<Long> listId = products.stream()
                    .map(product -> product.getMyProduct().getId())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(listId, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    /**
     * Retrieves a specific product for a user based on user ID and product ID.
     *
     * @param userID The ID of the user.
     * @param id     The ID of the product.
     * @return A ResponseEntity containing the product or NO_CONTENT if no data is found.
     */
    @GetMapping("/{userID}/{id}")
    public ResponseEntity<MyProduct> userCartByUserIdAndId(@PathVariable("userID") String userID, @PathVariable long id) {
        Optional<M> items = repository.findByMyCustomer_Id(userID);

        if (items.isPresent()) {
            List<H> products = items.get().getCustomerDataHelpers();
            if (products.stream().anyMatch(product -> product.getMyProduct().getId() == id)) {
                return new ResponseEntity<>(productRepository.findById(id).orElse(null), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    /**
     * Retrieves the user's cart, including product and inventory information.
     *
     * @param userID The ID of the user.
     * @return A ResponseEntity containing a list of ProductAndInventory or NO_CONTENT if no data is found.
     */
    @GetMapping("/{userID}")
    public ResponseEntity<List<ProductAndInventory>> userCartById(@PathVariable("userID") String userID) {
        Optional<M> item = repository.findByMyCustomer_Id(userID);

        if (item.isPresent()) {
            List<H> products = item.get().getCustomerDataHelpers();
            if (!products.isEmpty()) {
                List<ProductAndInventory> productAndInventoryList = products.stream()
                        .map(product -> new ProductAndInventory(
                                inventoryRepository.findById("00000" + product.getMyProduct().getId()).orElse(null).getQuantity(),
                                product.getMyProduct(),
                                product.getQuantity()))
                        .collect(Collectors.toList());
                return new ResponseEntity<>(productAndInventoryList, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    /**
     * Saves or updates user data based on the provided cart items.
     *
     * @param cartItems The cart items to be saved.
     * @param data      The customer data to be updated or created.
     * @param helper    The helper data associated with the customer data.
     * @return The updated or created customer data.
     */
    @PostMapping(value = "/save", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public M saveUserData(@RequestBody CustomerProduct cartItems, M data, H helper) {
        try {
            if (data instanceof CustomerCart) {
                helper.setQuantity(cartItems.getQuantity());
            }
            Optional<MyProduct> optionalProduct = productRepository.findById(cartItems.getProductId());
            if (optionalProduct.isPresent()) {
                MyProduct myProduct = optionalProduct.get();
                Optional<MyCustomer> optionalCustomer = customerRepository.findById(cartItems.getCustomerId());

                if (optionalCustomer.isPresent()) {
                    MyCustomer myCustomer = optionalCustomer.get();
                    M modelData;
                    Optional<M> modelDataInstance = repository.findByMyCustomer_Id(cartItems.getCustomerId());
                    modelData = modelDataInstance.orElse(data);
                    if (modelData.getCustomerDataHelpers().stream()
                            .anyMatch(sdata -> sdata.getMyProduct().getId() == cartItems.getProductId())) {
                        return null;
                    }
                    modelData.setMyCustomer(myCustomer);
                    helper.setMyProduct(myProduct);
                    customerDataHelperRepository.save(helper);
                    modelData.getCustomerDataHelpers().add(helper);
                    return repository.save(modelData);
                }

                helper.setMyProduct(myProduct);
                data.setMyCustomer(new MyCustomer(cartItems.getCustomerId()));
                customerDataHelperRepository.save(helper);
                data.getCustomerDataHelpers().add(helper);
            }
            return repository.save(data);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Deletes a specific cart item for a user based on user ID and product ID.
     *
     * @param userId The ID of the user.
     * @param id     The ID of the product.
     */
    @Transactional
    @DeleteMapping("/{userId}/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCartItem(@PathVariable("userId") String userId, @PathVariable("id") long id) {
        try {
            Optional<M> data = repository.findByMyCustomer_Id(userId);
            if (data.isPresent()) {
                List<H> products = data.get().getCustomerDataHelpers();
                listProductToDelete(id, (List<H>) products);
            }
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes all cart items for a user based on user ID.
     *
     * @param userId The ID of the user.
     */
    @DeleteMapping("/deleteAll/{userId}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllCartItems(@PathVariable("userId") String userId) {
        Optional<M> data = repository.findByMyCustomer_Id(userId);
        if (data.isPresent()) {
            repository.deleteAllByMyCustomer_Id(userId);
        }
    }

    /**
     * Deletes all cart items associated with a specific product ID after purchase.
     *
     * @param id The ID of the product.
     */
    @DeleteMapping("/delete-artwork/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAfterPurchase(@PathVariable long id) {
        try {
            Iterable<M> data = repository.findAll();
            for (M cart : data) {
                List<H> products = cart.getCustomerDataHelpers();
                listProductToDelete(id, (List<H>) products);
            }
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
    }

    private void listProductToDelete(@PathVariable long id, List<H> products) {
        Iterator<H> iterator = products.iterator();
        while (iterator.hasNext()) {
            H helper = iterator.next();
            if (productRepository.findById(id).get().getId() == helper.getMyProduct().getId()) {
                iterator.remove();
                customerDataHelperRepository.delete(helper);
            }
        }
    }
}
