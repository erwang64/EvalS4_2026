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
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CrewMemberController.class);

	private final CrewMemberService crewMemberService;

    public CrewMemberController(CrewMemberService crewMemberService) {
        this.crewMemberService = crewMemberService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Ajout d'un membre d'équipage", description = "Accessible aux rôles : ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CrewMemberResponse> createCrewMember(@RequestBody CrewMemberRequest request) throws EndPointException {
        LOGGER.debug("POST /api/crew-members");
        try {
            CrewMember entityToCreate = CrewMemberMapper.toEntity(request);
            CrewMember createdEntity = crewMemberService.addCrewMember(entityToCreate, request.getLunarBaseId());
            LOGGER.info("Membre d'équipage {} créé (201 CREATED)", createdEntity.getCrewMemberId());
            return ResponseEntity.status(HttpStatus.CREATED).body(CrewMemberMapper.toResponse(createdEntity));
        } catch (ServiceException e) {
            LOGGER.error("Erreur lors de la création d'un membre d'équipage", e);
            throw new EndPointException(HttpStatus.BAD_REQUEST, e.getMessage(), ResourceType.CREW_MEMBER, e);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un membre d'équipage par son ID", description = "Accessible aux rôles : USER, ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CrewMemberResponse> getCrewMemberById(@PathVariable("id") int id) throws EndPointException {
        LOGGER.debug("GET /api/crew-members/{}", id);
        try {
            CrewMember member = crewMemberService.getCrewMemberById(id);
            LOGGER.info("Membre d'équipage {} récupéré (200 OK)", id);
            return ResponseEntity.ok(CrewMemberMapper.toResponse(member));
        } catch (ServiceException e) {
            LOGGER.error("Membre d'équipage {} introuvable", id, e);
            throw new EndPointException(HttpStatus.NOT_FOUND, e.getMessage(), ResourceType.CREW_MEMBER, e);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/all")
    @Operation(summary = "Récupérer tous les membres d'équipage", description = "Accessible aux rôles : USER, ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<CrewMemberResponse>> getAllCrewMembers() {
        LOGGER.debug("GET /api/crew-members/all");
        Iterable<CrewMember> members = crewMemberService.getAllCrewMembers();
        List<CrewMemberResponse> responseList = new ArrayList<>();
        
        for (CrewMember member : members) {
            responseList.add(CrewMemberMapper.toResponse(member));
        }
        
        LOGGER.info("{} membre(s) d'équipage récupéré(s) (200 OK)", responseList.size());
        return ResponseEntity.ok(responseList);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Mise à jour d'un membre d'équipage", description = "Accessible aux rôles : ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CrewMemberResponse> updateCrewMember(
            @PathVariable("id") int id, 
            @RequestBody CrewMemberRequest request) throws EndPointException {
        LOGGER.debug("PUT /api/crew-members/{}", id);
        try {
            CrewMember memberDetails = CrewMemberMapper.toEntity(request);
            CrewMember updatedMember = crewMemberService.updateCrewMember(id, memberDetails);
            LOGGER.info("Membre d'équipage {} mis à jour (200 OK)", id);
            return ResponseEntity.ok(CrewMemberMapper.toResponse(updatedMember));
        } catch (ServiceException e) {
            LOGGER.error("Erreur lors de la mise à jour du membre d'équipage {}", id, e);
            throw new EndPointException(HttpStatus.BAD_REQUEST, e.getMessage(), ResourceType.CREW_MEMBER, e);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Suppression d'un membre d'équipage", description = "Accessible aux rôles : ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deleteCrewMember(@PathVariable("id") int id) throws EndPointException {
        LOGGER.debug("DELETE /api/crew-members/{}", id);
        try {
            crewMemberService.deleteCrewMember(id);
            LOGGER.info("Membre d'équipage {} supprimé (204 NO_CONTENT)", id);
            return ResponseEntity.noContent().build();
        } catch (ServiceException e) {
            LOGGER.error("Erreur lors de la suppression du membre d'équipage {}", id, e);
            throw new EndPointException(HttpStatus.NOT_FOUND, e.getMessage(), ResourceType.CREW_MEMBER, e);
        }
    }
}