package edu.esiea.LunarBaseApi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "SpaceSuit")
public class SpaceSuit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SpaceSuitId")
    private int spaceSuitId;

    @Column(name = "size", nullable = false)
    private int size; 

    @Column(name = "model", nullable = false)
    private String model; 

}