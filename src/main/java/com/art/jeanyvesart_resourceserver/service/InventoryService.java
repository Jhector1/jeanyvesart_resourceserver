package com.art.jeanyvesart_resourceserver.service;


import com.art.jeanyvesart_resourceserver.helper.InventoryManager;
import com.art.jeanyvesart_resourceserver.model.*;
import com.art.jeanyvesart_resourceserver.repository.*;
import com.art.jeanyvesart_resourceserver.site_data.paintings.*;
import com.art.jeanyvesart_resourceserver.site_data.print.GreenEnergy;
import com.mysql.cj.Session;
import jakarta.persistence.EntityManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@Transactional
public class InventoryService {
    private final static Paintings paintings = new Paintings();

    private final static List<Inventory> inventoryPaintings = new ArrayList<>();
    private final static List<Inventory> inventoryClothes = new ArrayList<>();
    private final static List<Inventory> inventoryPrint = new ArrayList<>();

    public static List<MyProduct> displayHealing_plantsSeries() {
        return paintings.getSeries(() -> new HealingPlants().getArtworkList());
    }


    public static List<MyProduct> displayEnlightenmentSeries() {
        return paintings.getSeries(() -> new Enlightenment().getArtworkList());

    }

    public static List<MyProduct> displayGreenEnergySeries() {
        return paintings.getSeries(() -> new GreenEnergy().getArtworkList());

    }

    public static List<MyProduct> displayLittleBlackAngelSeries() {
        return paintings.getSeries(() -> new LittleBlackAngel().getArtworkList());

    }

    public static List<MyProduct> displayJacketSeries() {
        return paintings.getSeries(() -> new Clothe().getArtworkList());

    }

    public static List<MyProduct> displayRenaissanceSeries() {
        return paintings.getSeries(() -> new Renaissance().getArtworkList());

    }

    public static List<MyProduct> allProducts() {
        ArrayList<MyProduct> artworks = new ArrayList<>();
        artworks.addAll(displayEnlightenmentSeries());
        artworks.addAll(displayHealing_plantsSeries());
        artworks.addAll(displayLittleBlackAngelSeries());
        artworks.addAll(displayRenaissanceSeries());
        artworks.addAll(displayJacketSeries());
        artworks.addAll(displayGreenEnergySeries());


        return artworks;
    }

    //    @Bean
//    public static CommandLineRunner allArtworkSave(InventoryRepository repo,
//                                                   OrderRepository orderRepository,
//                                                   CustomerFavoriteRepository customerFavoriteRepository,
//                                                   CustomerCartRepository customerCartRepository,
//                                                   CustomerRepository customerRepository) {
//        return args -> {
//
//            InventoryManager.initProductList(allProducts());
//            IntStream.range(0, allProducts().size()).forEach(InventoryManager::addToInventoryList);
//            repo.saveAll(InventoryManager.getInventoryList());
//
//
//        };
//    }
    public static void fillInventory(List<MyProduct> products, List<Inventory> inventories) {
        products.forEach(myProduct -> {

            Inventory inventory = new Inventory();
            inventory.setId("00000" + myProduct.getId());

            inventory.setQuantity(myProduct.getQuantity());
            inventory.setMyProduct(myProduct);
            inventories.add(inventory);

        });
    }
public static void fillAll(){
    fillInventory(displayLittleBlackAngelSeries(), inventoryPaintings);
   fillInventory(displayEnlightenmentSeries(), inventoryPaintings);
  fillInventory(displayHealing_plantsSeries(), inventoryPaintings);
  fillInventory(displayRenaissanceSeries(), inventoryPaintings);

    fillInventory(displayGreenEnergySeries(), inventoryPrint);
    fillInventory(displayJacketSeries(), inventoryClothes);
}
    @Bean

    public static CommandLineRunner allArtworkSave3(DepotInventoryRepository repo, InventoryRepository inventoryRepository, EntityManager entityManager) {
   fillAll();
        return args -> {
            // InventoryManager.initProductList(allProducts());
            // IntStream.range(0, allProducts().size()).forEach(InventoryManager::addToInventoryList);
            //InventoryManager.initProductList(allProducts());
            DepotInventory paintings = new DepotInventory();
            paintings.setCategory("Painting");

            paintings.setInventories(inventoryPaintings);
           inventoryRepository.saveAll(inventoryPaintings);

            DepotInventory prints = new DepotInventory();
            prints.setCategory("Print");
            inventoryRepository.saveAll(inventoryPrint);prints.setInventories(inventoryPrint);


            DepotInventory clothes = new DepotInventory();
            clothes.setCategory("Clothes");
            clothes.setInventories(inventoryClothes);
            inventoryRepository.saveAll(inventoryClothes);

            repo.save(paintings);
            repo.save(clothes);
            repo.save(prints);
//            entityManager.flush();
//            entityManager.clear();

        };
    }



    private String getSeries(String imageUrl) {
        if (imageUrl.toLowerCase().trim().contains("renaissance")) {
            return "renaissance";
        } else if (imageUrl.toLowerCase().trim().contains("jacket")) {
            return "jacket";
        }
        if (imageUrl.toLowerCase().trim().contains("little_black_angel")) {
            return "little_black_angel";
        } else if (imageUrl.toLowerCase().trim().contains("healing_plants")) {
            return "healing_plants";
        } else if (imageUrl.toLowerCase().trim().contains("enlightenment")) {
            return "enlightenment";
        } else if (imageUrl.toLowerCase().trim().contains("greenenergy")) {
            return "greenenergy";
        } else {
            return "";
        }
    }
}