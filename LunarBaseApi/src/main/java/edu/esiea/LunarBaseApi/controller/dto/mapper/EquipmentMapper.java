package edu.esiea.LunarBaseApi.controller.dto.mapper;

import edu.esiea.LunarBaseApi.controller.dto.EquipmentRequest;
import edu.esiea.LunarBaseApi.controller.dto.EquipmentResponse;
import edu.esiea.LunarBaseApi.model.Equipment;

public class EquipmentMapper {

	public static Equipment toEntity(EquipmentRequest req) {
        if (req == null) return null;
        
        Equipment entity = new Equipment();
        entity.setNameEquiment(req.getNameEquiment());
        entity.setEquipmentStatus(req.getEquipmentStatus());
        return entity;
    }

    public static EquipmentResponse toResponse(Equipment entity) {
        if (entity == null) return null;
        
        EquipmentResponse resp = new EquipmentResponse();
        resp.setEquipmentId(entity.getEquipmentId());
        resp.setNameEquiment(entity.getNameEquiment());
        resp.setEquipmentStatus(entity.getEquipmentStatus());
        return resp;
    }
    
}
