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

import edu.esiea.LunarBaseApi.enumeration.CrewrRole;
import edu.esiea.LunarBaseApi.exception.ServiceException;
import edu.esiea.LunarBaseApi.model.CrewMember;
import edu.esiea.LunarBaseApi.model.SpaceSuit;
import edu.esiea.LunarBaseApi.service.CrewMemberService;

@WebMvcTest(CrewMemberController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class CrewMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CrewMemberService crewMemberService;

    private CrewMember memberTest;

    @BeforeEach
    void setup() {
        SpaceSuit suit = new SpaceSuit();
        suit.setSize(42);
        suit.setModel("ModèleX");

        memberTest = new CrewMember();
        memberTest.setCrewMemberId(1);
        memberTest.setFirstName("Neil");
        memberTest.setLastName("Armstrong");
        memberTest.setCrewRole(CrewrRole.PILOTE);
        memberTest.setSpaceSuit(suit);
    }

    @Test
    void shouldGetCrewMemberById() throws Exception {
        when(crewMemberService.getCrewMemberById(1)).thenReturn(memberTest);

        mockMvc.perform(get("/api/crew-members/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.firstName").value("Neil"))
               .andExpect(jsonPath("$.spaceSuit.model").value("ModèleX"));

        verify(crewMemberService).getCrewMemberById(1);
    }

    @Test
    void shouldReturn404WhenCrewMemberNotFound() throws Exception {
        when(crewMemberService.getCrewMemberById(99)).thenThrow(new ServiceException("Introuvable"));

        mockMvc.perform(get("/api/crew-members/99"))
               .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetAllCrewMembers() throws Exception {
        CrewMember member2 = new CrewMember();
        member2.setCrewMemberId(2);
        member2.setFirstName("Buzz");
        member2.setLastName("Aldrin");

        when(crewMemberService.getAllCrewMembers()).thenReturn(List.of(memberTest, member2));

        mockMvc.perform(get("/api/crew-members/all"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(2))
               .andExpect(jsonPath("$[0].firstName").value("Neil"))
               .andExpect(jsonPath("$[1].firstName").value("Buzz"));

        verify(crewMemberService).getAllCrewMembers();
    }

    @Test
    void shouldCreateCrewMember() throws Exception {
        when(crewMemberService.addCrewMember(any(CrewMember.class), any())).thenReturn(memberTest);

        String json = """
                {
                    "firstName": "Neil",
                    "lastName": "Armstrong",
                    "crewRole": "PILOTE",
                    "spaceSuit": { "size": 42, "model": "ModèleX" }
                }
                """;

        mockMvc.perform(post("/api/crew-members")
               .contentType(MediaType.APPLICATION_JSON)
               .content(json))
               .andExpect(status().isCreated());

        verify(crewMemberService).addCrewMember(any(CrewMember.class), any());
    }

    @Test
    void shouldReturn400WhenCreateRefusedByService() throws Exception {
        when(crewMemberService.addCrewMember(any(CrewMember.class), any()))
                .thenThrow(new ServiceException("Base pleine"));

        String json = """
                {
                    "firstName": "Neil",
                    "lastName": "Armstrong",
                    "crewRole": "PILOTE",
                    "spaceSuit": { "size": 42, "model": "ModèleX" },
                    "lunarBaseId": 99
                }
                """;

        mockMvc.perform(post("/api/crew-members")
               .contentType(MediaType.APPLICATION_JSON)
               .content(json))
               .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteCrewMember() throws Exception {
        mockMvc.perform(delete("/api/crew-members/1"))
               .andExpect(status().isNoContent());

        verify(crewMemberService).deleteCrewMember(1);
    }

    @Test
    void shouldReturn404WhenDeleteNotFound() throws Exception {
        org.mockito.Mockito.doThrow(new ServiceException("Introuvable"))
                .when(crewMemberService).deleteCrewMember(anyInt());

        mockMvc.perform(delete("/api/crew-members/99"))
               .andExpect(status().isNotFound());
    }
}
