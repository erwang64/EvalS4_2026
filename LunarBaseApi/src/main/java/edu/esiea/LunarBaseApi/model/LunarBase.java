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

	public int getLunarBaseId() {
		return lunarBaseId;
	}

	public void setLunarBaseId(int lunarBaseId) {
		this.lunarBaseId = lunarBaseId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getMaximalCapacity() {
		return maximalCapacity;
	}

	public void setMaximalCapacity(int maximalCapacity) {
		this.maximalCapacity = maximalCapacity;
	}

	public List<CrewMember> getCrewMembers() {
		return crewMembers;
	}

	public void setCrewMembers(List<CrewMember> crewMembers) {
		this.crewMembers = crewMembers;
	}

	public List<Equipment> getEquipments() {
		return equipments;
	}

	public void setEquipments(List<Equipment> equipments) {
		this.equipments = equipments;
	}
	
    
    

}
