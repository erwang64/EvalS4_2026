package edu.esiea.LunarBaseApi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import edu.esiea.LunarBaseApi.model.LunarBase;
import edu.esiea.LunarBaseApi.service.LunarBaseService;

@WebMvcTest(LunarBaseController.class) // On teste uniquement ce contrôleur spécifique
@AutoConfigureMockMvc(addFilters = false) // On désactive les filtres de sécurité (JWT) pour tester le fonctionnel
@ActiveProfiles("test") // On utilise le profil de test
public class LunarBaseControllerTest {

    @Autowired
    private MockMvc mockMvc; // L'outil qui simule les requêtes HTTP

    // Dans Spring Boot 3.4+, @MockBean est remplacé par @MockitoBean (comme sur la slide 48 de ton cours !)
    @MockitoBean 
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

    // ==========================================
    // TEST GET BY ID
    // ==========================================
    @Test
    void shouldGetBaseById() throws Exception {
        // 1. Config du Mock
        when(lunarBaseService.getBaseById(1)).thenReturn(baseTest);

        // 2. Appel et Assertions
        mockMvc.perform(get("/api/lunar-bases/1"))
               .andExpect(status().isOk()) // Vérifie le statut HTTP 200
               .andExpect(jsonPath("$.name").value("Base Alpha")); // Vérifie le contenu JSON

        verify(lunarBaseService).getBaseById(1);
    }

    // ==========================================
    // TEST GET ALL
    // ==========================================
    @Test
    void shouldGetAllBases() throws Exception {
        LunarBase base2 = new LunarBase();
        base2.setLunarBaseId(2);
        base2.setName("Base Beta");

        List<LunarBase> bases = List.of(baseTest, base2);
        when(lunarBaseService.getAllBases()).thenReturn(bases);

        mockMvc.perform(get("/api/lunar-bases/all"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(2)) // Vérifie qu'on récupère bien 2 bases
               .andExpect(jsonPath("$[0].name").value("Base Alpha"))
               .andExpect(jsonPath("$[1].name").value("Base Beta"));

        verify(lunarBaseService).getAllBases();
    }

    // ==========================================
    // TEST CREATE (POST)
    // ==========================================
    @Test
    void shouldCreateBase() throws Exception {
        when(lunarBaseService.createLunarBase(any(LunarBase.class))).thenReturn(baseTest);

        // Le JSON "brut" que l'on simule d'envoyer, grâce à l'astuce des triples guillemets de ton cours
        String json = """
                {
                    "name": "Base Alpha",
                    "posX": 15,
                    "posY": 42,
                    "maximalCapacity": 50,
                    "sector": "Secteur Nord"
                }
                """;

        mockMvc.perform(post("/api/lunar-bases")
               .contentType(MediaType.APPLICATION_JSON)
               .content(json))
               .andExpect(status().isCreated()); // Vérifie le statut 201 CREATED

        verify(lunarBaseService).createLunarBase(any(LunarBase.class));
    }

    // ==========================================
    // TEST DELETE
    // ==========================================
    @Test
    void shouldDeleteBase() throws Exception {
        // Simuler un appel DELETE sur /api/lunar-bases/1
        mockMvc.perform(delete("/api/lunar-bases/1"))
               .andExpect(status().isNoContent()); // Vérifie le statut 204 NO CONTENT

        verify(lunarBaseService).deleteLunarBase(1);
    }
}
