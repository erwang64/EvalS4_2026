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
    private LunarBaseRepository lunarBaseRepo; // On crée le faux Repository (le Mock)

    @InjectMocks
    private LunarBaseService lunarBaseService; // Le vrai Service qu'on teste, dans lequel Spring va injecter le faux Repo

    private LunarBase baseTest;

    @BeforeEach
    void setup() {
        // On prépare une base de test pour nos scénarios
        baseTest = new LunarBase();
        baseTest.setLunarBaseId(1);
        baseTest.setName("Base Alpha");
        baseTest.setPosX(15);
        baseTest.setPosY(42);
        baseTest.setMaximalCapacity(50);
        baseTest.setSector("Secteur Nord");
    }

    // ==========================================
    // TEST DE LECTURE (GET)
    // ==========================================
    @Test
    public void testGetBaseById_Success() throws ServiceException {
        // 1. CONFIGURATION DU MOCK : On lui dit quoi répondre
        when(lunarBaseRepo.findById(1)).thenReturn(Optional.of(baseTest));

        // 2. ACT : On appelle notre vrai service
        LunarBase result = lunarBaseService.getBaseById(1);

        // 3. ASSERT : On vérifie que le service nous a bien retourné la base
        assertNotNull(result);
        assertEquals("Base Alpha", result.getName());
        
        // Vérification de sécurité : le service a-t-il bien appelé le repo ?
        verify(lunarBaseRepo).findById(1);
    }

    @Test
    public void testGetBaseById_NotFound() {
        // 1. CONFIGURATION DU MOCK : Le repo fait semblant de ne rien trouver (Optional vide)
        when(lunarBaseRepo.findById(99)).thenReturn(Optional.empty());

        // 2. ACT & ASSERT : Le service DOIT lever notre ServiceException personnalisée
        assertThrows(ServiceException.class, () -> {
            lunarBaseService.getBaseById(99);
        }, "Le service doit lever une exception si l'ID n'existe pas");
        
        verify(lunarBaseRepo).findById(99);
    }

 // ==========================================
    // TEST DE CREATION (POST)
    // ==========================================
    @Test
    public void testCreateLunarBase_Success() throws ServiceException {
        baseTest.setLunarBaseId(0); 

        // 1. CONFIGURATION DU MOCK 
        when(lunarBaseRepo.findByName("Base Alpha")).thenReturn(Optional.empty()); 
        when(lunarBaseRepo.save(any(LunarBase.class))).thenReturn(baseTest);

        // 2. ACT
        LunarBase createdBase = lunarBaseService.createLunarBase(baseTest);

        // 3. ASSERT
        assertNotNull(createdBase);
        assertEquals("Base Alpha", createdBase.getName());
        verify(lunarBaseRepo).save(baseTest); 
    }
}