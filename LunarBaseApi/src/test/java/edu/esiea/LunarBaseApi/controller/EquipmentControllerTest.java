package edu.esiea.LunarBaseApi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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

import edu.esiea.LunarBaseApi.enumeration.EquipmentStatus;
import edu.esiea.LunarBaseApi.exception.ServiceException;
import edu.esiea.LunarBaseApi.model.Equipment;
import edu.esiea.LunarBaseApi.service.EquipmentService;

@WebMvcTest(EquipmentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class EquipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
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
    void shouldGetEquipmentById() throws Exception {
        when(equipmentService.getEquipmentById(1)).thenReturn(equipmentTest);

        mockMvc.perform(get("/api/equipments/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.nameEquiment").value("Foreuse laser"));

        verify(equipmentService).getEquipmentById(1);
    }

    @Test
    void shouldReturn404WhenEquipmentNotFound() throws Exception {
        when(equipmentService.getEquipmentById(99)).thenThrow(new ServiceException("Introuvable"));

        mockMvc.perform(get("/api/equipments/99"))
               .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetAllEquipments() throws Exception {
        Equipment eq2 = new Equipment();
        eq2.setEquipmentId(2);
        eq2.setNameEquiment("Panneau solaire");
        eq2.setEquipmentStatus(EquipmentStatus.MAITENANCE);

        when(equipmentService.getAllEquipments()).thenReturn(List.of(equipmentTest, eq2));

        mockMvc.perform(get("/api/equipments/all"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(2))
               .andExpect(jsonPath("$[0].nameEquiment").value("Foreuse laser"))
               .andExpect(jsonPath("$[1].nameEquiment").value("Panneau solaire"));

        verify(equipmentService).getAllEquipments();
    }

    @Test
    void shouldCreateEquipment() throws Exception {
        when(equipmentService.addEquipment(any(Equipment.class), any())).thenReturn(equipmentTest);

        String json = """
                {
                    "nameEquiment": "Foreuse laser",
                    "equipmentStatus": "OPERATIONNEL"
                }
                """;

        mockMvc.perform(post("/api/equipments")
               .contentType(MediaType.APPLICATION_JSON)
               .content(json))
               .andExpect(status().isCreated());

        verify(equipmentService).addEquipment(any(Equipment.class), any());
    }

    @Test
    void shouldReturn400WhenCreateRefusedByService() throws Exception {
        when(equipmentService.addEquipment(any(Equipment.class), any()))
                .thenThrow(new ServiceException("Création refusée"));

        String json = """
                {
                    "nameEquiment": "Foreuse laser",
                    "equipmentStatus": "OPERATIONNEL",
                    "lunarBaseId": 99
                }
                """;

        mockMvc.perform(post("/api/equipments")
               .contentType(MediaType.APPLICATION_JSON)
               .content(json))
               .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteEquipment() throws Exception {
        mockMvc.perform(delete("/api/equipments/1"))
               .andExpect(status().isNoContent());

        verify(equipmentService).deleteEquipment(1);
    }

    @Test
    void shouldReturn404WhenDeleteNotFound() throws Exception {
        org.mockito.Mockito.doThrow(new ServiceException("Introuvable"))
                .when(equipmentService).deleteEquipment(anyInt());

        mockMvc.perform(delete("/api/equipments/99"))
               .andExpect(status().isNotFound());
    }
}
