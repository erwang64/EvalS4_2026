package edu.esiea.LunarBaseApi.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import edu.esiea.LunarBaseApi.controller.dto.CrewMemberRequest;
import edu.esiea.LunarBaseApi.controller.dto.CrewMemberResponse;
import edu.esiea.LunarBaseApi.controller.dto.error.EndPointException;
import edu.esiea.LunarBaseApi.controller.dto.error.ResourceType;
import edu.esiea.LunarBaseApi.controller.dto.mapper.CrewMemberMapper;
import edu.esiea.LunarBaseApi.exception.ServiceException;
import edu.esiea.LunarBaseApi.model.CrewMember;
import edu.esiea.LunarBaseApi.service.CrewMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/crew-members")
@Tag(name = "Membres d'équipage", description = "Gestion du personnel de la base lunaire")
public class CrewMemberController {
	
	private final CrewMemberService crewMemberService;

    public CrewMemberController(CrewMemberService crewMemberService) {
        this.crewMemberService = crewMemberService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Ajout d'un membre d'équipage", description = "Accessible aux rôles : ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CrewMemberResponse> createCrewMember(@RequestBody CrewMemberRequest request) throws EndPointException {
        try {
            CrewMember entityToCreate = CrewMemberMapper.toEntity(request);
            CrewMember createdEntity = crewMemberService.addCrewMember(entityToCreate, request.getLunarBaseId());
            return ResponseEntity.status(HttpStatus.CREATED).body(CrewMemberMapper.toResponse(createdEntity));
        } catch (ServiceException e) {
            // Remplacer ResourceType.CREW_MEMBER par la bonne valeur de votre Enum si elle s'appelle autrement
            throw new EndPointException(HttpStatus.BAD_REQUEST, e.getMessage(), ResourceType.CREW_MEMBER, e);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un membre d'équipage par son ID", description = "Accessible aux rôles : USER, ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CrewMemberResponse> getCrewMemberById(@PathVariable("id") int id) throws EndPointException {
        try {
            CrewMember member = crewMemberService.getCrewMemberById(id);
            return ResponseEntity.ok(CrewMemberMapper.toResponse(member));
        } catch (ServiceException e) {
            throw new EndPointException(HttpStatus.NOT_FOUND, e.getMessage(), ResourceType.CREW_MEMBER, e);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/all")
    @Operation(summary = "Récupérer tous les membres d'équipage", description = "Accessible aux rôles : USER, ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<CrewMemberResponse>> getAllCrewMembers() {
        Iterable<CrewMember> members = crewMemberService.getAllCrewMembers();
        List<CrewMemberResponse> responseList = new ArrayList<>();
        
        for (CrewMember member : members) {
            responseList.add(CrewMemberMapper.toResponse(member));
        }
        
        return ResponseEntity.ok(responseList);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Mise à jour d'un membre d'équipage", description = "Accessible aux rôles : ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CrewMemberResponse> updateCrewMember(
            @PathVariable("id") int id, 
            @RequestBody CrewMemberRequest request) throws EndPointException {
        try {
            CrewMember memberDetails = CrewMemberMapper.toEntity(request);
            CrewMember updatedMember = crewMemberService.updateCrewMember(id, memberDetails);
            return ResponseEntity.ok(CrewMemberMapper.toResponse(updatedMember));
        } catch (ServiceException e) {
            throw new EndPointException(HttpStatus.BAD_REQUEST, e.getMessage(), ResourceType.CREW_MEMBER, e);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Suppression d'un membre d'équipage", description = "Accessible aux rôles : ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deleteCrewMember(@PathVariable("id") int id) throws EndPointException {
        try {
            crewMemberService.deleteCrewMember(id);
            return ResponseEntity.noContent().build();
        } catch (ServiceException e) {
            throw new EndPointException(HttpStatus.NOT_FOUND, e.getMessage(), ResourceType.CREW_MEMBER, e);
        }
    }
}