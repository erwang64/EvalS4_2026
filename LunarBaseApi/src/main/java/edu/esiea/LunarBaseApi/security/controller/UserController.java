package edu.esiea.LunarBaseApi.security.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.esiea.LunarBaseApi.controller.dto.UserRequest;
import edu.esiea.LunarBaseApi.controller.dto.UserResponse;
import edu.esiea.LunarBaseApi.controller.dto.error.EndPointException;
import edu.esiea.LunarBaseApi.controller.dto.error.ResourceType;
import edu.esiea.LunarBaseApi.controller.dto.mapper.UserMapper;
import edu.esiea.LunarBaseApi.model.User;
import edu.esiea.LunarBaseApi.security.service.JwtService;
import edu.esiea.LunarBaseApi.security.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    // --- 1. S'INSCRIRE ---
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest request) throws Exception {
        // 1. Traduction du DTO en Entité
        User userToCreate = UserMapper.toEntity(request);
        
        // 2. Sauvegarde via le Service
        User createdUser = this.userService.register(userToCreate);
        
        // 3. Traduction de l'Entité en DTO de réponse (pour masquer le mot de passe !)
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toResponse(createdUser));
    }

    // --- 2. SE CONNECTER ---
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody UserRequest request) throws EndPointException {
        
        // Vérification avec le DTO Request
        if (request.getUsername() == null || request.getPassword() == null || request.getUsername().isBlank() || request.getPassword().isBlank()) {
            throw new EndPointException(HttpStatus.BAD_REQUEST, "Login ou mot de passe non fourni", ResourceType.USER, null);
        }

        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        String jwt = this.jwtService.generateToken(userDetails);

        return ResponseEntity.noContent()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .build();
    }
    

}
