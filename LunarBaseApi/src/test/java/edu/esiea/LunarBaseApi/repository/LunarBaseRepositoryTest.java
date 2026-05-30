package edu.esiea.LunarBaseApi.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import edu.esiea.LunarBaseApi.model.LunarBase;

@DataJpaTest
@ActiveProfiles("test")
public class LunarBaseRepositoryTest {

    @Autowired
    private LunarBaseRepository lunarBaseRepo;

    private static final String BASE_NAME = "Base Alpha";
    private static final int BASE_POS_X = 15;
    private static final int BASE_POS_Y = 42;
    private static final int BASE_CAPACITY = 50;
    private static final String BASE_SECTOR = "Secteur Nord";

    @BeforeEach
    void setup() {
        lunarBaseRepo.deleteAll();

        LunarBase base = new LunarBase();
        base.setName(BASE_NAME);
        base.setPosX(BASE_POS_X);
        base.setPosY(BASE_POS_Y);
        base.setMaximalCapacity(BASE_CAPACITY);
        base.setSector(BASE_SECTOR);

        lunarBaseRepo.save(base);
    }

    @Test
    public void testFindByName() {
        Optional<LunarBase> ret = lunarBaseRepo.findByName(BASE_NAME);
        assertTrue(ret.isPresent(), "On devrait retrouver la base par son nom");
        assertEquals(BASE_NAME, ret.get().getName(), "Le nom n'est pas correct");

        ret = lunarBaseRepo.findByName("Base Inconnue");
        assertTrue(ret.isEmpty(), "On ne devrait pas trouver de base avec ce nom");

        ret = lunarBaseRepo.findByName(null);
        assertTrue(ret.isEmpty(), "On ne devrait pas trouver de base avec null");
    }

    @Test
    public void testFindByPosXAndPosY() {
        Optional<LunarBase> ret = lunarBaseRepo.findByPosXAndPosY(BASE_POS_X, BASE_POS_Y);
        assertTrue(ret.isPresent(), "On devrait retrouver la base par sa position");
        assertEquals(BASE_NAME, ret.get().getName(), "Le nom n'est pas correct");

        ret = lunarBaseRepo.findByPosXAndPosY(999, 999);
        assertTrue(ret.isEmpty(), "On ne devrait pas trouver de base à cette position");
    }

    @Test
    public void testEntityRestrictions() {
        LunarBase baseNullName = new LunarBase();
        baseNullName.setName(null);
        baseNullName.setPosX(100);
        baseNullName.setPosY(200);
        baseNullName.setMaximalCapacity(10);
        baseNullName.setSector("Secteur Sud");

        assertThrows(DataIntegrityViolationException.class, () -> {
            lunarBaseRepo.saveAndFlush(baseNullName);
        }, "Le nom d'une base ne devrait pas être nul");

        LunarBase baseDuplicate = new LunarBase();
        baseDuplicate.setName(BASE_NAME);
        baseDuplicate.setPosX(100);
        baseDuplicate.setPosY(200);
        baseDuplicate.setMaximalCapacity(10);
        baseDuplicate.setSector("Secteur Est");

        assertThrows(DataIntegrityViolationException.class, () -> {
            lunarBaseRepo.saveAndFlush(baseDuplicate);
        }, "Deux bases avec le même nom ne devraient pas être possibles");
    }
}
