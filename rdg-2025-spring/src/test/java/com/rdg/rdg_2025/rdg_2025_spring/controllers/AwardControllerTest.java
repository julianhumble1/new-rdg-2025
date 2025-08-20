package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Award;
import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;
import com.rdg.rdg_2025.rdg_2025_spring.models.Person;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.award.AwardRequest;
import com.rdg.rdg_2025.rdg_2025_spring.services.AwardService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AwardController.class)
public class AwardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AwardService awardService;

    @InjectMocks
    private AwardController awardController;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    private Award testAward;
    private ObjectNode requestJson;

    @BeforeEach
    public void setupContext() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private Award makeAward(int id, String name) {
        Festival f = new Festival();
        f.setId(1);
        Person p = new Person();
        p.setId(2);
        Production pr = new Production();
        pr.setId(3);
        return new Award(id, name, f, p, pr);
    }

    private AwardRequest makeRequest() {
        AwardRequest req = new AwardRequest();
        req.setName("Best Design");
        req.setFestivalId(1);
        req.setPersonId(2);
        req.setProductionId(3);
        return req;
    }

    @BeforeEach
    public void setup() {
        testAward = makeAward(5, "Outstanding Achievement");

        requestJson = objectMapper.createObjectNode();
        requestJson.put("name", "Best Design");
        requestJson.put("festivalId", 1);
        requestJson.put("personId", 2);
        requestJson.put("productionId", 3);
    }

    @Test
    public void addNewAward_success_returnsCreated() throws Exception {
        AwardRequest req = makeRequest();
        Award created = makeAward(10, req.getName());

        when(awardService.addNewAward(any(AwardRequest.class))).thenReturn(created);

        mockMvc.perform(post("/awards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/awards/10"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.award.id").value(10))
                .andExpect(jsonPath("$.award.name").value("Best Design"));
    }

    @Test
    public void addNewAward_databaseError_returns500() throws Exception {
        AwardRequest req = makeRequest();

        when(awardService.addNewAward(any(AwardRequest.class))).thenThrow(new DatabaseException("db error"));

        mockMvc.perform(post("/awards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("db error"));
    }

    @Test
    public void getAwardById_success_returns200() throws Exception {
        when(awardService.getAwardById(5)).thenReturn(testAward);

        mockMvc.perform(get("/awards/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.award.id").value(5))
                .andExpect(jsonPath("$.award.name").value("Outstanding Achievement"));
    }

    @Test
    public void getAwardById_notFound_returns404() throws Exception {
        when(awardService.getAwardById(99)).thenThrow(new EntityNotFoundException("award not found"));

        mockMvc.perform(get("/awards/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("award not found"));
    }

    @Test
    public void updateAward_success_returns200() throws Exception {
        AwardRequest req = makeRequest();
        Award updated = makeAward(7, req.getName());

        when(awardService.updateAward(eq(7), any(AwardRequest.class))).thenReturn(updated);

        mockMvc.perform(patch("/awards/7")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.award.id").value(7))
                .andExpect(jsonPath("$.award.name").value("Best Design"));
    }

    @Test
    public void updateAward_notFound_returns404() throws Exception {
        AwardRequest req = makeRequest();

        when(awardService.updateAward(eq(77), any(AwardRequest.class)))
                .thenThrow(new EntityNotFoundException("award not found"));

        mockMvc.perform(patch("/awards/77")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("award not found"));
    }

    @Test
    public void deleteAward_success_returnsNoContent() throws Exception {
        doNothing().when(awardService).deleteAwardById(3);

        mockMvc.perform(delete("/awards/3"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteAward_notFound_returns404() throws Exception {
        doThrow(new EntityNotFoundException("award not found")).when(awardService).deleteAwardById(33);

        mockMvc.perform(delete("/awards/33"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("award not found"));
    }
}