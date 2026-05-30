package edu.esiea.LunarBaseApi.controller.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import edu.esiea.LunarBaseApi.controller.dto.LunarBaseRequest;
import edu.esiea.LunarBaseApi.controller.dto.LunarBaseResponse;
import edu.esiea.LunarBaseApi.model.CrewMember;
import edu.esiea.LunarBaseApi.model.LunarBase;

public class LunarBaseMapper {
	
// 1. DTO entrant -> Entité (Pour la création POST)
	public static LunarBase toEntity(LunarBaseRequest dto) {
		LunarBase base = new LunarBase();
		if (dto.getName() != null)            { base.setName(dto.getName()); }
		if (dto.getSector() != null)          { base.setSector(dto.getSector()); }
		if (dto.getPosX() != null)            { base.setPosX(dto.getPosX()); }
		if (dto.getPosY() != null)            { base.setPosY(dto.getPosY()); }
		if (dto.getMaximalCapacity() != null) { base.setMaximalCapacity(dto.getMaximalCapacity()); }
		return base;
	}

	// 2. DTO entrant -> Entité existante (Pour la mise à jour PATCH)
	public static LunarBase patchWithRequest(LunarBaseRequest dto, LunarBase origin) {
		if (dto.getName() != null)            { origin.setName(dto.getName()); }
		if (dto.getSector() != null)          { origin.setSector(dto.getSector()); }
		if (dto.getPosX() != null)            { origin.setPosX(dto.getPosX()); }
		if (dto.getPosY() != null)            { origin.setPosY(dto.getPosY()); }
		if (dto.getMaximalCapacity() != null) { origin.setMaximalCapacity(dto.getMaximalCapacity()); }
		return origin;
	}

	// 3. Entité -> DTO sortant (Pour l'affichage d'une seule base)
	public static LunarBaseResponse toResponse(LunarBase base) {
		LunarBaseResponse ret = new LunarBaseResponse();
		
		// Attention : Utilise getLunarBaseId() ou getId() selon ce que tu as mis dans ton modèle
		ret.setLunarBaseId(base.getLunarBaseId()); 
		ret.setName(base.getName());
		ret.setSector(base.getSector());
		ret.setPosX(base.getPosX());
		ret.setPosY(base.getPosY());
		ret.setMaximalCapacity(base.getMaximalCapacity());
		
		// L'astuce du prof : On ne renvoie pas tout l'objet CrewMember, juste son ID !
		List<Integer> crewIds = new ArrayList<>();
			if (base.getCrewMembers() != null && !base.getCrewMembers().isEmpty()) {
				for (CrewMember member : base.getCrewMembers()) {
					// C'est ICI qu'on utilise le getter de votre classe CrewMember :
					crewIds.add(member.getCrewMemberId()); 
				}
			}
			ret.setCrewIds(crewIds);
			ret.setCurrentCrewCount(crewIds.size());
		
		return ret;
	}

	// 4. Liste d'Entités -> Liste de DTOs (Pour l'affichage de toutes les bases)
	public static List<LunarBaseResponse> toResponseList(Iterable<LunarBase> bases) {
		List<LunarBaseResponse> result = new ArrayList<>();
		for (LunarBase base : bases) {
			result.add(toResponse(base));
		}
		return result;
	}

}
