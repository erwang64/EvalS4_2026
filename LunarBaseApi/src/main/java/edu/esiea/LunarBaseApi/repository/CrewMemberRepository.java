package edu.esiea.LunarBaseApi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.esiea.LunarBaseApi.model.CrewMember;

@Repository
public interface CrewMemberRepository extends JpaRepository<CrewMember, Integer> {

    /**
     * Reads a crew member using their first name and last name.
     *
     * @param firstName The first name of the crew member to find
     * @param lastName The last name of the crew member to find
     * @return Found crew member as an {@link Optional}.
     */
    Optional<CrewMember> findByFirstNameAndLastName(String firstName, String lastName);

}