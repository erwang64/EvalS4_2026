package edu.esiea.LunarBaseApi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.esiea.LunarBaseApi.exception.ServiceException;
import edu.esiea.LunarBaseApi.model.LunarBase;
import edu.esiea.LunarBaseApi.repository.LunarBaseRepository;

@ExtendWith(MockitoExtension.class)
public class LunarBaseServiceTest {

    @Mock
    private LunarBaseRepository lunarBaseRepo;

    @InjectMocks
    private LunarBaseService lunarBaseService;

    private LunarBase baseTest;

    @BeforeEach
    void setup() {
        baseTest = new LunarBase();
        baseTest.setLunarBaseId(1);
        baseTest.setName("Base Alpha");
        baseTest.setPosX(15);
        baseTest.setPosY(42);
        baseTest.setMaximalCapacity(50);
        baseTest.setSector("Secteur Nord");
    }

    @Test
    public void testGetBaseById_Success() throws ServiceException {
        when(lunarBaseRepo.findById(1)).thenReturn(Optional.of(baseTest));

        LunarBase result = lunarBaseService.getBaseById(1);

        assertNotNull(result);
        assertEquals("Base Alpha", result.getName());
        verify(lunarBaseRepo).findById(1);
    }

    @Test
    public void testGetBaseById_NotFound() {
        when(lunarBaseRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> {
            lunarBaseService.getBaseById(99);
        }, "Le service doit lever une exception si l'ID n'existe pas");

        verify(lunarBaseRepo).findById(99);
    }

    @Test
    public void testCreateLunarBase_Success() throws ServiceException {
        baseTest.setLunarBaseId(0);

        when(lunarBaseRepo.findByName("Base Alpha")).thenReturn(Optional.empty());
        when(lunarBaseRepo.findByPosXAndPosY(15, 42)).thenReturn(Optional.empty());
        when(lunarBaseRepo.save(any(LunarBase.class))).thenReturn(baseTest);

        LunarBase createdBase = lunarBaseService.createLunarBase(baseTest);

        assertNotNull(createdBase);
        assertEquals("Base Alpha", createdBase.getName());
        verify(lunarBaseRepo).save(baseTest);
    }

    @Test
    public void testCreateLunarBase_IdDejaDefini() {
        baseTest.setLunarBaseId(5);

        assertThrows(ServiceException.class, () -> {
            lunarBaseService.createLunarBase(baseTest);
        }, "Créer une base avec un ID déjà défini doit être impossible");
    }

    @Test
    public void testCreateLunarBase_NomDejaPris() {
        baseTest.setLunarBaseId(0);
        when(lunarBaseRepo.findByName("Base Alpha")).thenReturn(Optional.of(baseTest));

        assertThrows(ServiceException.class, () -> {
            lunarBaseService.createLunarBase(baseTest);
        }, "Deux bases avec le même nom ne devraient pas être possibles");
    }

    @Test
    public void testCreateLunarBase_PositionOccupee() {
        baseTest.setLunarBaseId(0);
        when(lunarBaseRepo.findByName("Base Alpha")).thenReturn(Optional.empty());
        when(lunarBaseRepo.findByPosXAndPosY(15, 42)).thenReturn(Optional.of(baseTest));

        assertThrows(ServiceException.class, () -> {
            lunarBaseService.createLunarBase(baseTest);
        }, "Créer une base à une position déjà occupée doit être impossible");
    }

    @Test
    public void testUpdateLunarBase_Success() throws ServiceException {
        when(lunarBaseRepo.findById(1)).thenReturn(Optional.of(baseTest));
        when(lunarBaseRepo.save(any(LunarBase.class))).thenReturn(baseTest);

        LunarBase nouvellesDonnees = new LunarBase();
        nouvellesDonnees.setName("Base Alpha");
        nouvellesDonnees.setSector("Secteur Sud");
        nouvellesDonnees.setMaximalCapacity(99);

        LunarBase result = lunarBaseService.updateLunarBase(1, nouvellesDonnees);

        assertNotNull(result);
        verify(lunarBaseRepo).save(baseTest);
    }

    @Test
    public void testUpdateLunarBase_NotFound() {
        when(lunarBaseRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> {
            lunarBaseService.updateLunarBase(99, baseTest);
        }, "Modifier une base inexistante doit être impossible");
    }

    @Test
    public void testUpdateLunarBase_NomDejaPris() {
        when(lunarBaseRepo.findById(1)).thenReturn(Optional.of(baseTest));
        when(lunarBaseRepo.findByName("Base Beta")).thenReturn(Optional.of(new LunarBase()));

        LunarBase nouvellesDonnees = new LunarBase();
        nouvellesDonnees.setName("Base Beta");

        assertThrows(ServiceException.class, () -> {
            lunarBaseService.updateLunarBase(1, nouvellesDonnees);
        }, "Renommer une base vers un nom déjà pris doit être impossible");
    }

    @Test
    public void testDeleteLunarBase_Success() throws ServiceException {
        when(lunarBaseRepo.existsById(1)).thenReturn(true);

        lunarBaseService.deleteLunarBase(1);

        verify(lunarBaseRepo).deleteById(1);
    }

    @Test
    public void testDeleteLunarBase_NotFound() {
        when(lunarBaseRepo.existsById(99)).thenReturn(false);

        assertThrows(ServiceException.class, () -> {
            lunarBaseService.deleteLunarBase(99);
        }, "Supprimer une base inexistante doit être impossible");
    }

    @Test
    public void testGetAllBases() {
        when(lunarBaseRepo.findAll()).thenReturn(new java.util.ArrayList<LunarBase>());

        lunarBaseService.getAllBases();

        verify(lunarBaseRepo).findAll();
    }
}
