package edu.esiea.LunarBaseApi.controller.dto;

import edu.esiea.LunarBaseApi.controller.dto.error.ResourceType;
import edu.esiea.LunarBaseApi.enumeration.EquipmentStatus;

public class EquipmentRequest extends AbstractRequest {
	
	private String nameEquiment;
    private EquipmentStatus equipmentStatus;
    
    // Pour l'affecter directement à une base !
    private Integer lunarBaseId; 

    public EquipmentRequest() {
        super(ResourceType.EQUIPMENT);
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

	public Integer getLunarBaseId() {
		return lunarBaseId;
	}

	public void setLunarBaseId(Integer lunarBaseId) {
		this.lunarBaseId = lunarBaseId;
	}
    
    

}
