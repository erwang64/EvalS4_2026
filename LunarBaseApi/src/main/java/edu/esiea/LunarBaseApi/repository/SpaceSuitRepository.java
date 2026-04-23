package edu.esiea.LunarBaseApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.esiea.LunarBaseApi.model.SpaceSuit;


@Repository
public interface SpaceSuitRepository extends JpaRepository<SpaceSuit, Integer> {
	
	/**
	 * Standard CRUD operations are sufficient as suits are managed via CrewMembers.
	 */

}
