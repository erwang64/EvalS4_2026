package edu.esiea.LunarBaseApi.security.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.esiea.LunarBaseApi.model.User;
import edu.esiea.LunarBaseApi.repository.UserRepository;


@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	private final UserRepository userRepository; 

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> opt = this.userRepository.findByUsername(username);

        if (opt.isEmpty()) {
            throw new UsernameNotFoundException("Aucun utilisateur avec le pseudo [" + username + "]");
        }

        User user = opt.get();

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole().name())
                .build();
    }

}
