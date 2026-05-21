package edu.esiea.LunarBaseApi.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import edu.esiea.LunarBaseApi.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	
	
    Optional<User> findByUsername(String username);
    
    boolean existsByUsername(String username);

}
