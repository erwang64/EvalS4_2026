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
import edu.esiea.LunarBaseApi.controller.dto.EquipmentRequest;
import edu.esiea.LunarBaseApi.controller.dto.EquipmentResponse;
import edu.esiea.LunarBaseApi.controller.dto.error.EndPointException;
import edu.esiea.LunarBaseApi.controller.dto.error.ResourceType;
import edu.esiea.LunarBaseApi.controller.dto.mapper.EquipmentMapper;
import edu.esiea.LunarBaseApi.exception.ServiceException;
import edu.esiea.LunarBaseApi.model.Equipment;
import edu.esiea.LunarBaseApi.service.EquipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/equipments")
@Tag(name = "Equipements", description = "Gestion du matériel et des outils de la base lunaire")
public class EquipmentController {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(EquipmentController.class);

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Création d'un Équipement", description = "Accessible aux rôles : ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<EquipmentResponse> createEquipment(@RequestBody EquipmentRequest request) throws EndPointException {
        LOGGER.debug("POST /api/equipments");
        try {
            Equipment entityToCreate = EquipmentMapper.toEntity(request);
            Equipment createdEntity = equipmentService.addEquipment(entityToCreate, request.getLunarBaseId());
            LOGGER.info("Équipement {} créé (201 CREATED)", createdEntity.getEquipmentId());
            return ResponseEntity.status(HttpStatus.CREATED).body(EquipmentMapper.toResponse(createdEntity));
        } catch (ServiceException e) {
            LOGGER.error("Erreur lors de la création d'un équipement", e);
            throw new EndPointException(HttpStatus.BAD_REQUEST, e.getMessage(), ResourceType.EQUIPMENT, e);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un équipement par son ID", description = "Accessible aux rôles : USER, ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<EquipmentResponse> getEquipmentById(@PathVariable("id") int id) throws EndPointException {
        LOGGER.debug("GET /api/equipments/{}", id);
        try {
            Equipment equipment = equipmentService.getEquipmentById(id);
            LOGGER.info("Équipement {} récupéré (200 OK)", id);
            return ResponseEntity.ok(EquipmentMapper.toResponse(equipment));
        } catch (ServiceException e) {
            LOGGER.error("Équipement {} introuvable", id, e);
            throw new EndPointException(HttpStatus.NOT_FOUND, e.getMessage(), ResourceType.EQUIPMENT, e);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/all")
    @Operation(summary = "Récupérer tous les équipements", description = "Accessible aux rôles : USER, ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<EquipmentResponse>> getAllEquipments() {
        LOGGER.debug("GET /api/equipments/all");
        Iterable<Equipment> equipments = equipmentService.getAllEquipments();
        List<EquipmentResponse> responseList = new ArrayList<>();
        
        for (Equipment eq : equipments) {
            responseList.add(EquipmentMapper.toResponse(eq));
        }
        
        LOGGER.info("{} équipement(s) récupéré(s) (200 OK)", responseList.size());
        return ResponseEntity.ok(responseList);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Mise à jour d'un équipement", description = "Accessible aux rôles : ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<EquipmentResponse> updateEquipment(
            @PathVariable("id") int id, 
            @RequestBody EquipmentRequest request) throws EndPointException {
        LOGGER.debug("PUT /api/equipments/{}", id);
        try {
            Equipment equipmentDetails = EquipmentMapper.toEntity(request);
            Equipment updatedEquipment = equipmentService.updateEquipment(id, equipmentDetails);
            LOGGER.info("Équipement {} mis à jour (200 OK)", id);
            return ResponseEntity.ok(EquipmentMapper.toResponse(updatedEquipment));
        } catch (ServiceException e) {
            LOGGER.error("Erreur lors de la mise à jour de l'équipement {}", id, e);
            throw new EndPointException(HttpStatus.BAD_REQUEST, e.getMessage(), ResourceType.EQUIPMENT, e);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Suppression d'un équipement", description = "Accessible aux rôles : ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deleteEquipment(@PathVariable("id") int id) throws EndPointException {
        LOGGER.debug("DELETE /api/equipments/{}", id);
        try {
            equipmentService.deleteEquipment(id);
            LOGGER.info("Équipement {} supprimé (204 NO_CONTENT)", id);
            return ResponseEntity.noContent().build();
        } catch (ServiceException e) {
            LOGGER.error("Erreur lors de la suppression de l'équipement {}", id, e);
            throw new EndPointException(HttpStatus.NOT_FOUND, e.getMessage(), ResourceType.EQUIPMENT, e);
        }
    }
}