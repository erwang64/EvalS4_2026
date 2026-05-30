package edu.esiea.LunarBaseApi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.esiea.LunarBaseApi.enumeration.EquipmentStatus;
import edu.esiea.LunarBaseApi.exception.ServiceException;
import edu.esiea.LunarBaseApi.model.Equipment;
import edu.esiea.LunarBaseApi.model.LunarBase;
import edu.esiea.LunarBaseApi.repository.EquipmentRepository;
import edu.esiea.LunarBaseApi.repository.LunarBaseRepository;

@ExtendWith(MockitoExtension.class)
public class EquipmentServiceTest {

    @Mock
    private EquipmentRepository equipmentRepo;

    @Mock
    private LunarBaseRepository lunarBaseRepo;

    @InjectMocks
    private EquipmentService equipmentService;

    private Equipment equipmentTest;

    @BeforeEach
    void setup() {
        equipmentTest = new Equipment();
        equipmentTest.setEquipmentId(1);
        equipmentTest.setNameEquiment("Foreuse laser");
        equipmentTest.setEquipmentStatus(EquipmentStatus.OPERATIONNEL);
    }

    @Test
    public void testAddEquipment_SansBase() throws ServiceException {
        when(equipmentRepo.save(equipmentTest)).thenReturn(equipmentTest);

        Equipment result = equipmentService.addEquipment(equipmentTest, null);

        assertNotNull(result);
        verify(equipmentRepo).save(equipmentTest);
    }

    @Test
    public void testAddEquipment_AvecBase() throws ServiceException {
        LunarBase base = new LunarBase();
        base.setLunarBaseId(10);
        base.setEquipments(new ArrayList<>());

        when(lunarBaseRepo.findById(10)).thenReturn(Optional.of(base));

        Equipment result = equipmentService.addEquipment(equipmentTest, 10);

        assertNotNull(result);
        assertEquals(1, base.getEquipments().size());
        verify(lunarBaseRepo).save(base);
    }

    @Test
    public void testAddEquipment_BaseInexistante() {
        when(lunarBaseRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> {
            equipmentService.addEquipment(equipmentTest, 99);
        }, "Affecter un équipement à une base inexistante doit être impossible");
    }

    @Test
    public void testGetEquipmentById_Success() throws ServiceException {
        when(equipmentRepo.findById(1)).thenReturn(Optional.of(equipmentTest));

        Equipment result = equipmentService.getEquipmentById(1);

        assertNotNull(result);
        assertEquals("Foreuse laser", result.getNameEquiment());
        verify(equipmentRepo).findById(1);
    }

    @Test
    public void testGetEquipmentById_NotFound() {
        when(equipmentRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> {
            equipmentService.getEquipmentById(99);
        }, "Un équipement introuvable doit lever une exception");
    }

    @Test
    public void testGetAllEquipments() {
        when(equipmentRepo.findAll()).thenReturn(new ArrayList<Equipment>());

        equipmentService.getAllEquipments();

        verify(equipmentRepo).findAll();
    }

    @Test
    public void testUpdateEquipment_Success() throws ServiceException {
        when(equipmentRepo.findById(1)).thenReturn(Optional.of(equipmentTest));
        when(equipmentRepo.save(equipmentTest)).thenReturn(equipmentTest);

        Equipment nouvellesDonnees = new Equipment();
        nouvellesDonnees.setNameEquiment("Foreuse laser v2");
        nouvellesDonnees.setEquipmentStatus(EquipmentStatus.MAITENANCE);

        Equipment result = equipmentService.updateEquipment(1, nouvellesDonnees);

        assertNotNull(result);
        assertEquals("Foreuse laser v2", result.getNameEquiment());
        verify(equipmentRepo).save(equipmentTest);
    }

    @Test
    public void testUpdateEquipment_NotFound() {
        when(equipmentRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> {
            equipmentService.updateEquipment(99, equipmentTest);
        }, "Modifier un équipement inexistant doit être impossible");
    }

    @Test
    public void testDeleteEquipment_Success() throws ServiceException {
        when(equipmentRepo.findById(1)).thenReturn(Optional.of(equipmentTest));

        equipmentService.deleteEquipment(1);

        verify(equipmentRepo).delete(equipmentTest);
    }

    @Test
    public void testDeleteEquipment_NotFound() {
        when(equipmentRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> {
            equipmentService.deleteEquipment(99);
        }, "Supprimer un équipement inexistant doit être impossible");
    }
}
