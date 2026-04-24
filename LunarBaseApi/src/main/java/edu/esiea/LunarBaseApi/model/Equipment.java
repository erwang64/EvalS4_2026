package edu.esiea.LunarBaseApi.model;

import edu.esiea.LunarBaseApi.enumeration.EquipmentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Equipment")
public class Equipment {
	
	@Id
	@Column(name = "EquipmentId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int equipmentId;
	
	@Column(name = "NameEquiment", nullable = false)
	private String NameEquiment;

	@Enumerated(EnumType.STRING)
	@Column(name = "EquipmentStatus", nullable = false)
	private EquipmentStatus equipmentStatus;

	public int getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(int equipmentId) {
		this.equipmentId = equipmentId;
	}

	public String getNameEquiment() {
		return NameEquiment;
	}

	public void setNameEquiment(String nameEquiment) {
		NameEquiment = nameEquiment;
	}

	public EquipmentStatus getEquipmentStatus() {
		return equipmentStatus;
	}

	public void setEquipmentStatus(EquipmentStatus equipmentStatus) {
		this.equipmentStatus = equipmentStatus;
	}
	
	
	

}
