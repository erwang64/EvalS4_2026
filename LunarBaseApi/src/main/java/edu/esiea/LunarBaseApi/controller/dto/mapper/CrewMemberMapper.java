package edu.esiea.LunarBaseApi.controller.dto.mapper;

import edu.esiea.LunarBaseApi.controller.dto.CrewMemberRequest;
import edu.esiea.LunarBaseApi.controller.dto.CrewMemberResponse;
import edu.esiea.LunarBaseApi.model.CrewMember;


public class CrewMemberMapper {

	public static CrewMember toEntity(CrewMemberRequest req) {
        CrewMember entity = new CrewMember();
        entity.setFirstName(req.getFirstName());
        entity.setLastName(req.getLastName());
        entity.setCrewRole(req.getCrewRole());

        if (req.getSpaceSuit() != null) {
            entity.setSpaceSuit(SpaceSuitMapper.toEntity(req.getSpaceSuit()));
        }
        
        return entity;
    }

    public static CrewMemberResponse toResponse(CrewMember entity) {
        CrewMemberResponse resp = new CrewMemberResponse();
        resp.setCrewMemberId(entity.getCrewMemberId());
        resp.setFirstName(entity.getFirstName());
        resp.setLastName(entity.getLastName());
        resp.setCrewRole(entity.getCrewRole());

        if (entity.getSpaceSuit() != null) {
            resp.setSpaceSuit(SpaceSuitMapper.toResponse(entity.getSpaceSuit()));
        }
        
        return resp;
    }
    
}
