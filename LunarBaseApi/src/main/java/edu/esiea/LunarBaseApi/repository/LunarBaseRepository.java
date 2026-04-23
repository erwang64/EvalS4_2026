package edu.esiea.LunarBaseApi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.esiea.LunarBaseApi.model.LunarBase;

@Repository
public interface LunarBaseRepository extends JpaRepository<LunarBase, Integer> {
	
	/**
	 * Reads a base using its name.
	 *
	 * @param name The name of the system to find
	 * @return Found system as an {@link Optional}.
	 */
	Optional<LunarBase> findByName(String name);

	/**
	 * Reads a system using its position.
	 *
	 * @param posX Position's X coordinate
	 * @param posY Position's Y coordinate
	 * @return Found base as an {@link Optional}.
	 */
	Optional<LunarBase> findByPosXAndPosY(int posX, int posY);

}
