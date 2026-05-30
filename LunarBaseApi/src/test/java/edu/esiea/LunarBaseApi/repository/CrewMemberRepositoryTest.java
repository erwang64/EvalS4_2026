package edu.esiea.LunarBaseApi.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import edu.esiea.LunarBaseApi.enumeration.CrewrRole;
import edu.esiea.LunarBaseApi.model.CrewMember;
import edu.esiea.LunarBaseApi.model.SpaceSuit;

@DataJpaTest
@ActiveProfiles("test")
public class CrewMemberRepositoryTest {

    @Autowired
    private CrewMemberRepository crewMemberRepo;

    private static final String FIRST_NAME = "Neil";
    private static final String LAST_NAME = "Armstrong";

    @BeforeEach
    void setup() {
        crewMemberRepo.deleteAll();

        SpaceSuit suit = new SpaceSuit();
        suit.setSize(42);
        suit.setModel("ModèleX");

        CrewMember member = new CrewMember();
        member.setFirstName(FIRST_NAME);
        member.setLastName(LAST_NAME);
        member.setCrewRole(CrewrRole.PILOTE);
        member.setSpaceSuit(suit);

        crewMemberRepo.save(member);
    }

    @Test
    public void testFindByFirstNameAndLastName() {
        Optional<CrewMember> ret = crewMemberRepo.findByFirstNameAndLastName(FIRST_NAME, LAST_NAME);
        assertTrue(ret.isPresent(), "On devrait retrouver le membre par son nom et prénom");
        assertEquals(FIRST_NAME, ret.get().getFirstName(), "Le prénom n'est pas correct");

        ret = crewMemberRepo.findByFirstNameAndLastName("Jean", "Inconnu");
        assertTrue(ret.isEmpty(), "On ne devrait pas trouver de membre inexistant");

        ret = crewMemberRepo.findByFirstNameAndLastName(null, null);
        assertTrue(ret.isEmpty(), "On ne devrait pas trouver de membre avec null");
    }

    @Test
    public void testEntityRestrictions() {
        CrewMember sansPrenom = new CrewMember();
        sansPrenom.setFirstName(null);
        sansPrenom.setLastName("Doe");

        assertThrows(DataIntegrityViolationException.class, () -> {
            crewMemberRepo.saveAndFlush(sansPrenom);
        }, "Le prénom d'un membre ne devrait pas être nul");
    }
}
