package edu.esiea.LunarBaseApi.controller.dto;

import edu.esiea.LunarBaseApi.enumeration.EquipmentStatus;

public class EquipmentResponse {

	
	private int equipmentId;
    private String nameEquiment;
    private EquipmentStatus equipmentStatus;
    
	public int getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(int equipmentId) {
		this.equipmentId = equipmentId;
	}
	public String getNameEquiment() {
		return nameEquiment;
	}
	public void setNameEquiment(String nameEquiment) {
		this.nameEquiment = nameEquiment;
	}
	public EquipmentStatus getEquipmentStatus() {
		return equipmentStatus;
	}
	public void setEquipmentStatus(EquipmentStatus equipmentStatus) {
		this.equipmentStatus = equipmentStatus;
	}
    
    
    
}
