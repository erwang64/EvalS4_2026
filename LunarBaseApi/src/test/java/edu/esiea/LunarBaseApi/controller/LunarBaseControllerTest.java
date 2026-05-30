package edu.esiea.LunarBaseApi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import edu.esiea.LunarBaseApi.exception.ServiceException;
import edu.esiea.LunarBaseApi.model.LunarBase;
import edu.esiea.LunarBaseApi.service.LunarBaseService;

@WebMvcTest(LunarBaseController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class LunarBaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

    @Test
    void shouldGetBaseById() throws Exception {
        when(lunarBaseService.getBaseById(1)).thenReturn(baseTest);

        mockMvc.perform(get("/api/lunar-bases/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Base Alpha"));

        verify(lunarBaseService).getBaseById(1);
    }

    @Test
    void shouldGetAllBases() throws Exception {
        LunarBase base2 = new LunarBase();
        base2.setLunarBaseId(2);
        base2.setName("Base Beta");

        List<LunarBase> bases = List.of(baseTest, base2);
        when(lunarBaseService.getAllBases()).thenReturn(bases);

        mockMvc.perform(get("/api/lunar-bases/all"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(2))
               .andExpect(jsonPath("$[0].name").value("Base Alpha"))
               .andExpect(jsonPath("$[1].name").value("Base Beta"));

        verify(lunarBaseService).getAllBases();
    }

    @Test
    void shouldCreateBase() throws Exception {
        when(lunarBaseService.createLunarBase(any(LunarBase.class))).thenReturn(baseTest);

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
               .andExpect(status().isCreated());

        verify(lunarBaseService).createLunarBase(any(LunarBase.class));
    }

    @Test
    void shouldUpdateBase() throws Exception {
        when(lunarBaseService.updateLunarBase(eq(1), any(LunarBase.class))).thenReturn(baseTest);

        String json = """
                {
                    "name": "Base Alpha",
                    "posX": 15,
                    "posY": 42,
                    "maximalCapacity": 99,
                    "sector": "Secteur Sud"
                }
                """;

        mockMvc.perform(put("/api/lunar-bases/1")
               .contentType(MediaType.APPLICATION_JSON)
               .content(json))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Base Alpha"));

        verify(lunarBaseService).updateLunarBase(eq(1), any(LunarBase.class));
    }

    @Test
    void shouldDeleteBase() throws Exception {
        mockMvc.perform(delete("/api/lunar-bases/1"))
               .andExpect(status().isNoContent());

        verify(lunarBaseService).deleteLunarBase(1);
    }

    @Test
    void shouldReturn404WhenBaseNotFound() throws Exception {
        when(lunarBaseService.getBaseById(99)).thenThrow(new ServiceException("Introuvable"));

        mockMvc.perform(get("/api/lunar-bases/99"))
               .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenCreateRefusedByService() throws Exception {
        when(lunarBaseService.createLunarBase(any(LunarBase.class)))
                .thenThrow(new ServiceException("Création refusée"));

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
               .andExpect(status().isBadRequest());
    }
}
