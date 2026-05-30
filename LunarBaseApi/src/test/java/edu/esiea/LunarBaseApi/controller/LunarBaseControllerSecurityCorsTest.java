package edu.esiea.LunarBaseApi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.esiea.LunarBaseApi.model.LunarBase;
import edu.esiea.LunarBaseApi.service.LunarBaseService;

@SpringBootTest
@ActiveProfiles({ "test", "JWTtest" })
class LunarBaseControllerSecurityCorsTest {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private LunarBaseService lunarBaseService;

    private MockMvc mockMvc;

    private static final String ANGULAR_ORIGIN = "http://localhost:4200";
    private static final String AUTRE_ORIGIN = "http://esiea.fr";

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldDenyCreateForUser() throws Exception {
        String json = """
                { "name": "Base Alpha", "posX": 1, "posY": 2, "maximalCapacity": 10, "sector": "Nord" }
                """;

        mockMvc.perform(post("/api/lunar-bases")
                .header("Origin", ANGULAR_ORIGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
               .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldDenyDeleteForUser() throws Exception {
        mockMvc.perform(delete("/api/lunar-bases/1")
                .header("Origin", ANGULAR_ORIGIN))
               .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldAllowGetForUser() throws Exception {
        when(lunarBaseService.getBaseById(1)).thenReturn(new LunarBase());

        mockMvc.perform(get("/api/lunar-bases/1")
                .header("Origin", ANGULAR_ORIGIN))
               .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAllowCreateForAdmin() throws Exception {
        LunarBase base = new LunarBase();
        base.setLunarBaseId(1);
        base.setName("Base Alpha");
        when(lunarBaseService.createLunarBase(any(LunarBase.class))).thenReturn(base);

        String json = """
                { "name": "Base Alpha", "posX": 1, "posY": 2, "maximalCapacity": 10, "sector": "Nord" }
                """;

        mockMvc.perform(post("/api/lunar-bases")
                .header("Origin", ANGULAR_ORIGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
               .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldAllowCorsForAngularOrigin() throws Exception {
        when(lunarBaseService.getBaseById(1)).thenReturn(new LunarBase());

        mockMvc.perform(get("/api/lunar-bases/1")
                .header("Origin", ANGULAR_ORIGIN))
               .andExpect(status().isOk())
               .andExpect(header().string("Access-Control-Allow-Origin", ANGULAR_ORIGIN));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldRejectCorsForUnknownOrigin() throws Exception {
        mockMvc.perform(get("/api/lunar-bases/1")
                .header("Origin", AUTRE_ORIGIN))
               .andExpect(status().isForbidden())
               .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }
}
