package edu.esiea.LunarBaseApi.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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

@RestController
@RequestMapping("/api/lunar-bases")
public class LunarBaseController {
	
	private final LunarBaseService service;
	
	public LunarBaseController(LunarBaseService service) {
		this.service = service;
	}
	
	
	@PostMapping
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
    
	
	

}
