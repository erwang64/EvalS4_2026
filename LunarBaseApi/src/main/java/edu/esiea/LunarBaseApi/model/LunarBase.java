package edu.esiea.LunarBaseApi.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "LunarBase")
public class LunarBase {
	@Id
	@Column(name = "LunarBaseId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int lunarBaseId;
	
	@Column(name = "name", nullable = false, unique = true)
	private String name;
	
	@Column(name = "sector", nullable = false)
	private String sector;
	
	@Column(name = "posY", nullable = false)
	private int posY;
	
	@Column(name = "posX", nullable = false)
	private int posX;
	
	@Column(name = "maximalCapacity", nullable = false)
	private int maximalCapacity;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "lunar_base_id") // Crée une clé étrangère dans la table CrewMember
    private List<CrewMember> crewMembers;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "lunar_base_id") // Crée une clé étrangère dans la table Equipment
    private List<Equipment> equipments;
	

}
