package com.art.jeanyvesart_resourceserver.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepotInventory {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_sequence_generator1")
    @SequenceGenerator(name = "my_sequence_generator1", sequenceName = "my_sequence1", initialValue = 0, allocationSize = 1)
    @Id
    private Long id;
    private String category;
    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true)//{CascadeType.MERGE,CascadeType.REMOVE, CascadeType.REFRESH})
    private List<Inventory> inventories;
}

