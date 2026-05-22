package edu.esiea.LunarBaseApi.controller.dto.mapper;

import edu.esiea.LunarBaseApi.controller.dto.SpaceSuitRequest;
import edu.esiea.LunarBaseApi.controller.dto.SpaceSuitResponse;
import edu.esiea.LunarBaseApi.model.SpaceSuit;

public class SpaceSuitMapper {

	public static SpaceSuit toEntity(SpaceSuitRequest req) {
        if (req == null) return null; // Sécurité au cas où l'astronaute n'a pas de combinaison
        
        SpaceSuit suit = new SpaceSuit();
        suit.setSize(req.getSize());
        suit.setModel(req.getModel());
        return suit;
    }

    public static SpaceSuitResponse toResponse(SpaceSuit entity) {
        if (entity == null) return null;
        
        SpaceSuitResponse resp = new SpaceSuitResponse();
        resp.setSpaceSuitId(entity.getSpaceSuitId());
        resp.setSize(entity.getSize());
        resp.setModel(entity.getModel());
        return resp;
    }
    
}
