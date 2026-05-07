package edu.esiea.LunarBaseApi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> createLunarBase(@RequestBody LunarBase baseToCreate) {
        try {

            LunarBase createdBase = service.createLunarBase(baseToCreate);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBase);
            
        } catch (ServiceException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
	
	

}
