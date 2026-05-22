package edu.esiea.LunarBaseApi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.esiea.LunarBaseApi.controller.dto.CrewMemberRequest;
import edu.esiea.LunarBaseApi.controller.dto.CrewMemberResponse;
import edu.esiea.LunarBaseApi.controller.dto.mapper.CrewMemberMapper;
import edu.esiea.LunarBaseApi.model.CrewMember;
import edu.esiea.LunarBaseApi.service.CrewMemberService;

@RestController
@RequestMapping("/api/crew-members")
public class CrewMemberController {
	
	private final CrewMemberService crewMemberService;

    public CrewMemberController(CrewMemberService crewMemberService) {
        this.crewMemberService = crewMemberService;
    }

    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CrewMemberResponse> createCrewMember(@RequestBody CrewMemberRequest request) throws Exception {
        
        CrewMember entityToCreate = CrewMemberMapper.toEntity(request);
        CrewMember createdEntity = crewMemberService.addCrewMember(entityToCreate, request.getLunarBaseId());
        return ResponseEntity.status(HttpStatus.CREATED).body(CrewMemberMapper.toResponse(createdEntity));
    }

}
