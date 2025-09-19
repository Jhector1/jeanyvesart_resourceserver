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

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class CustomerDataController<H extends CustomerDataHelper, R extends CustomerDataRepository<H, M>, M extends CustomerData<H>> {
    private final R repository;
    private final InventoryRepository inventoryRepository;
    private final CustomerDataHelperRepository<H> customerDataHelperRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public CustomerDataController(
            R repository,
            ProductRepository productRepository,
            CustomerRepository customerRepository,
            InventoryRepository inventoryRepository,
            CustomerDataHelperRepository<H> customerDataHelperRepository
    ) {
        this.repository = repository;
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.customerDataHelperRepository = customerDataHelperRepository;
    }

    // ---------- helpers ----------
    private static String padInvKey(long id) {
        return String.format("%05d", id);
    }

    private static <K> Map<K, Integer> qtyIndex(Iterable<Inventory> invs, Function<Inventory, K> keyFn) {
        Map<K, Integer> m = new HashMap<>();
        for (Inventory inv : invs) m.put(keyFn.apply(inv), inv.getQuantity());
        return m;
    }

    // ---------- endpoints ----------

    @GetMapping("/product_id/{userID}")
    @Transactional(readOnly = true)
    public ResponseEntity<List<Long>> getUserDataByUserId(@PathVariable("userID") String userID) {
        Optional<M> items = repository.findByMyCustomer_Id(userID);

        if (items.isPresent()) {
            List<Long> listId = items.get().getCustomerDataHelpers().stream()
                    .map(h -> h.getMyProduct() != null ? h.getMyProduct().getId() : null)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(listId, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{userID}/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<MyProduct> userCartByUserIdAndId(@PathVariable("userID") String userID, @PathVariable long id) {
        Optional<M> items = repository.findByMyCustomer_Id(userID);
        if (items.isPresent()) {
            boolean hasIt = items.get().getCustomerDataHelpers().stream()
                    .anyMatch(h -> h.getMyProduct() != null && h.getMyProduct().getId() == id);
            if (hasIt) {
                Optional<MyProduct> p = productRepository.findById(id);
                if (p.isPresent()) return new ResponseEntity<>(p.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{userID}")
    @Transactional(readOnly = true)
    public ResponseEntity<List<ProductAndInventory>> userCartById(@PathVariable("userID") String userID) {
        Optional<M> item = repository.findByMyCustomer_Id(userID);
        if (item.isPresent()) {
            List<H> helpers = item.get().getCustomerDataHelpers();
            if (!helpers.isEmpty()) {
                // Batch inventory to avoid N+1
                List<Long> productIds = helpers.stream()
                        .map(h -> h.getMyProduct() != null ? h.getMyProduct().getId() : null)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                List<String> invIds = productIds.stream().map(CustomerDataController::padInvKey).collect(Collectors.toList());
                Map<String, Integer> qtyById = qtyIndex(inventoryRepository.findAllById(invIds), Inventory::getId);

                List<ProductAndInventory> out = helpers.stream()
                        .filter(h -> h.getMyProduct() != null)
                        .map(h -> {
                            long pid = h.getMyProduct().getId();
                            int stockQty = qtyById.getOrDefault(padInvKey(pid), 0);
                            return new ProductAndInventory(stockQty, h.getMyProduct(), h.getQuantity());
                        })
                        .collect(Collectors.toList());

                return new ResponseEntity<>(out, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/save", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public M saVeUserCart(@RequestBody CustomerProduct cartItems, M data, H helper) {
        try {
            // quantity is primitive int → never null; clamp to >= 0 once
            final int requestedQty = Math.max(0, cartItems.getQuantity());

            if (data instanceof CustomerCart) {
                helper.setQuantity(requestedQty);
            }

            Optional<MyProduct> optionalProduct = productRepository.findById(cartItems.getProductId());
            if (!optionalProduct.isPresent()) {
                return data; // product missing: keep current behavior minimal
            }
            MyProduct myProduct = optionalProduct.get();

            // get or create customer
            MyCustomer myCustomer = customerRepository.findById(cartItems.getCustomerId())
                    .orElseGet(() -> new MyCustomer(cartItems.getCustomerId()));

            // get or use provided model instance
            M modelData = repository.findByMyCustomer_Id(cartItems.getCustomerId()).orElse(data);
            modelData.setMyCustomer(myCustomer);

            // reuse existing helper for same product (avoid duplicates)
            H existing = null;
            for (H h : modelData.getCustomerDataHelpers()) {
                if (h.getMyProduct() != null && h.getMyProduct().getId() == myProduct.getId()) {
                    existing = h;
                    break;
                }
            }

            if (existing != null) {
                existing.setQuantity(requestedQty);
                return repository.save(modelData);
            }

            // new helper for this product
            helper.setMyProduct(myProduct);
            helper.setQuantity(requestedQty);
            customerDataHelperRepository.save(helper);
            modelData.getCustomerDataHelpers().add(helper);

            return repository.save(modelData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    @DeleteMapping("/{userId}/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCartItem(@PathVariable("userId") String userId, @PathVariable("id") long id) {
        try {
            Optional<M> data = repository.findByMyCustomer_Id(userId);
            if (data.isPresent()) {
                List<H> helpers = data.get().getCustomerDataHelpers();
                List<H> toDelete = helpers.stream()
                        .filter(h -> h.getMyProduct() != null && h.getMyProduct().getId() == id)
                        .collect(Collectors.toList());
                if (!toDelete.isEmpty()) {
                    helpers.removeAll(toDelete);
                    customerDataHelperRepository.deleteAll(toDelete);
                }
            }
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
    }

    @DeleteMapping("/deleteAll/{userId}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllCartItem(@PathVariable("userId") String id) {
        Optional<M> data = repository.findByMyCustomer_Id(id);
        if (data.isPresent()) {
            repository.deleteAllByMyCustomer_Id(id);
        }
    }

    @DeleteMapping("/delete-artwork/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAfterPurchase(@PathVariable long id) {
        try {
            // If available, prefer: customerDataHelperRepository.deleteByMyProduct_Id(id);
            Iterable<M> data = repository.findAll();

            List<H> toDelete = new ArrayList<>();
            for (M cart : data) {
                List<H> helpers = cart.getCustomerDataHelpers();
                if (helpers != null && !helpers.isEmpty()) {
                    for (H h : helpers) {
                        if (h.getMyProduct() != null && h.getMyProduct().getId() == id) {
                            toDelete.add(h);
                        }
                    }
                }
            }

            if (!toDelete.isEmpty()) {
                for (M cart : data) {
                    List<H> helpers = cart.getCustomerDataHelpers();
                    if (helpers != null && !helpers.isEmpty()) {
                        helpers.removeIf(h -> h.getMyProduct() != null && h.getMyProduct().getId() == id);
                    }
                }
                customerDataHelperRepository.deleteAll(toDelete);
            }
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
    }
}
