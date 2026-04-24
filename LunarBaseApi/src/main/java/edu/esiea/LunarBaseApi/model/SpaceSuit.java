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

	public int getSpaceSuitId() {
		return spaceSuitId;
	}

	public void setSpaceSuitId(int spaceSuitId) {
		this.spaceSuitId = spaceSuitId;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	} 
    
    
    

}