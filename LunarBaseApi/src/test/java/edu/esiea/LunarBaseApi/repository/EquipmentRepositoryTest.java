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

import edu.esiea.LunarBaseApi.enumeration.EquipmentStatus;
import edu.esiea.LunarBaseApi.model.Equipment;

@DataJpaTest
@ActiveProfiles("test")
public class EquipmentRepositoryTest {

    @Autowired
    private EquipmentRepository equipmentRepo;

    private static final String EQ_NAME = "Foreuse laser";

    @BeforeEach
    void setup() {
        equipmentRepo.deleteAll();

        Equipment eq = new Equipment();
        eq.setNameEquiment(EQ_NAME);
        eq.setEquipmentStatus(EquipmentStatus.OPERATIONNEL);
        equipmentRepo.save(eq);
    }

    @Test
    public void testSaveAndFindById() {
        Equipment eq = new Equipment();
        eq.setNameEquiment("Panneau solaire");
        eq.setEquipmentStatus(EquipmentStatus.MAITENANCE);
        eq = equipmentRepo.save(eq);

        Optional<Equipment> ret = equipmentRepo.findById(eq.getEquipmentId());
        assertTrue(ret.isPresent(), "On devrait retrouver l'équipement sauvegardé");
        assertEquals("Panneau solaire", ret.get().getNameEquiment());
    }

    @Test
    public void testEntityRestrictions() {
        Equipment sansNom = new Equipment();
        sansNom.setNameEquiment(null);
        sansNom.setEquipmentStatus(EquipmentStatus.OPERATIONNEL);
        assertThrows(DataIntegrityViolationException.class, () -> {
            equipmentRepo.saveAndFlush(sansNom);
        }, "Le nom d'un équipement ne devrait pas être nul");

        Equipment sansStatut = new Equipment();
        sansStatut.setNameEquiment("Radar");
        sansStatut.setEquipmentStatus(null);
        assertThrows(DataIntegrityViolationException.class, () -> {
            equipmentRepo.saveAndFlush(sansStatut);
        }, "Le statut d'un équipement ne devrait pas être nul");
    }
}
