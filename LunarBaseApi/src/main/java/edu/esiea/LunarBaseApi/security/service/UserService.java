package edu.esiea.LunarBaseApi.security.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.esiea.LunarBaseApi.enumeration.Role;
import edu.esiea.LunarBaseApi.exception.ServiceException;
import edu.esiea.LunarBaseApi.model.User;
import edu.esiea.LunarBaseApi.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; 

    // Injection par constructeur
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(User newUser) throws ServiceException {
        if (userRepository.existsByUsername(newUser.getUsername())) {
            throw new ServiceException("Erreur : Le pseudo est déjà utilisé.");
        }
        String encryptedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encryptedPassword);
        if (newUser.getRole() == null) {
            newUser.setRole(Role.USER);
        }
        return userRepository.save(newUser);
    }

    public User loadUserByUsername(String username) throws ServiceException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceException("Utilisateur introuvable."));
    }
    
}
