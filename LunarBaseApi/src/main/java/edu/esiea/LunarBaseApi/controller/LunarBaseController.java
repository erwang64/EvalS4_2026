package edu.esiea.LunarBaseApi.controller;

import java.net.URI;

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

import edu.esiea.LunarBaseApi.controller.dto.LunarBaseRequest;
import edu.esiea.LunarBaseApi.controller.dto.LunarBaseResponse;
import edu.esiea.LunarBaseApi.controller.dto.error.EndPointException;
import edu.esiea.LunarBaseApi.controller.dto.error.ResourceType;
import edu.esiea.LunarBaseApi.controller.dto.mapper.LunarBaseMapper;
import edu.esiea.LunarBaseApi.exception.ServiceException;
import edu.esiea.LunarBaseApi.model.LunarBase;
import edu.esiea.LunarBaseApi.service.LunarBaseService;

// Imports Swagger / OpenAPI ajoutés
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/lunar-bases")
public class LunarBaseController {
	
	private final LunarBaseService service;
	
	public LunarBaseController(LunarBaseService service) {
		this.service = service;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	@Operation(summary = "Création d'une base lunaire", description = "Accessible aux rôles : ADMIN")
	@SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<LunarBaseResponse> createLunarBase(@RequestBody LunarBaseRequest request) throws EndPointException {
        
        // 1. Traduction
        LunarBase baseToCreate = LunarBaseMapper.toEntity(request);
        
        // 2. Sauvegarde
        try {
            baseToCreate = this.service.createLunarBase(baseToCreate);
        } catch (ServiceException e) {
            throw new EndPointException(HttpStatus.BAD_REQUEST, e.getMessage(), ResourceType.LUNAR_BASE, e);
        }
        
        // 3. Création de l'adresse (URI)
        URI uri = org.springframework.web.servlet.support.ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(baseToCreate.getLunarBaseId()) 
                .toUri();
        
        // 4. Renvoi du résultat avec l'URI
        return ResponseEntity.created(uri).body(LunarBaseMapper.toResponse(baseToCreate));
    }
	
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@GetMapping("/{id}")
	@Operation(summary = "Récupérer une base lunaire par son ID", description = "Accessible aux rôles : USER, ADMIN")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<LunarBaseResponse> getLunarBaseById(@PathVariable("id") int id) throws EndPointException {
	    try {
	        // 1. On demande au service
	        LunarBase base = service.getBaseById(id);
	        
	        // 2. On traduit en Response
	        LunarBaseResponse response = LunarBaseMapper.toResponse(base);
	        
	        // 3. On renvoie 200 OK
	        return ResponseEntity.ok(response);
	        
	    } catch (ServiceException e) {
	        // Si l'ID n'existe pas, on renvoie une 404 (Not Found)
	        throw new EndPointException(HttpStatus.NOT_FOUND, e.getMessage(), ResourceType.LUNAR_BASE, e);
	    }
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@GetMapping("/all")
	@Operation(summary = "Récupérer toutes les bases lunaires", description = "Accessible aux rôles : USER, ADMIN")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<java.util.List<LunarBaseResponse>> getAllLunarBases() {
		// 1. On récupère toutes les entités
		Iterable<LunarBase> bases = service.getAllBases();
		
		// 2. On crée une liste vide pour nos réponses
		java.util.List<LunarBaseResponse> responseList = new java.util.ArrayList<>();
		
		// 3. On traduit chaque base l'une après l'autre
		for (LunarBase base : bases) {
			responseList.add(LunarBaseMapper.toResponse(base));
		}
		
		// 4. On renvoie la liste avec un code 200 OK
		return ResponseEntity.ok(responseList);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	@Operation(summary = "Mise à jour d'une base lunaire", description = "Accessible aux rôles : ADMIN")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<LunarBaseResponse> updateLunarBase(
			@PathVariable("id") int id, 
			@RequestBody LunarBaseRequest request) throws EndPointException {
		
		try {
			// 1. On traduit le JSON reçu en Entité "brouillon"
			LunarBase baseDetails = LunarBaseMapper.toEntity(request);
			
			// 2. On envoie l'ID et les nouvelles données au Service
			LunarBase updatedBase = service.updateLunarBase(id, baseDetails);
			
			// 3. On traduit le résultat et on renvoie un 200 OK
			return ResponseEntity.ok(LunarBaseMapper.toResponse(updatedBase));
			
		} catch (ServiceException e) {
			// Si le service refuse (ID inconnu ou nom déjà pris)
			throw new EndPointException(HttpStatus.BAD_REQUEST, e.getMessage(), ResourceType.LUNAR_BASE, e);
		}
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	@Operation(summary = "Suppression d'une base lunaire", description = "Accessible aux rôles : ADMIN")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<Void> deleteLunarBase(@PathVariable("id") int id) throws EndPointException {
		try {
			service.deleteLunarBase(id);
			return ResponseEntity.noContent().build();
			
		} catch (ServiceException e) {
			throw new EndPointException(HttpStatus.NOT_FOUND, e.getMessage(), ResourceType.LUNAR_BASE, e);
		}
	}

}