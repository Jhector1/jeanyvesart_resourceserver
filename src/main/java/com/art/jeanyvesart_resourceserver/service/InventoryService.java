package com.art.jeanyvesart_resourceserver.service;


import com.art.jeanyvesart_resourceserver.helper.InventoryManager;
import com.art.jeanyvesart_resourceserver.model.*;
import com.art.jeanyvesart_resourceserver.repository.*;
import com.art.jeanyvesart_resourceserver.site_data.paintings.*;
import com.art.jeanyvesart_resourceserver.site_data.print.GreenEnergy;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class InventoryService {
    private final static Paintings paintings = new Paintings();


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

    @Bean
    public static CommandLineRunner allArtworkSave(InventoryRepository repo,
                                                   OrderRepository orderRepository,
                                                   CustomerFavoriteRepository customerFavoriteRepository,
                                                   CustomerCartRepository customerCartRepository,
                                                   CustomerRepository customerRepository) {
        return args -> {

            InventoryManager.initProductList(allProducts());
            IntStream.range(0, allProducts().size()).forEach(InventoryManager::addToInventoryList);
            repo.saveAll(InventoryManager.getInventoryList());


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