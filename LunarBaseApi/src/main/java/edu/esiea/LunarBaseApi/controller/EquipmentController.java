package edu.esiea.LunarBaseApi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.esiea.LunarBaseApi.controller.dto.EquipmentRequest;
import edu.esiea.LunarBaseApi.controller.dto.EquipmentResponse;
import edu.esiea.LunarBaseApi.controller.dto.mapper.EquipmentMapper;
import edu.esiea.LunarBaseApi.model.Equipment;
import edu.esiea.LunarBaseApi.service.EquipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/equipments")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @Operation(summary = "Création d'un Equipement", description = "Accessible aux rôles : USER, ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EquipmentResponse> createEquipment(@RequestBody EquipmentRequest request) throws Exception {
        
        Equipment entityToCreate = EquipmentMapper.toEntity(request);
        
        Equipment createdEntity = equipmentService.addEquipment(entityToCreate, request.getLunarBaseId());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(EquipmentMapper.toResponse(createdEntity));
    }
}
