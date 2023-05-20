package com.task.lottery.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.task.lottery.controllers.ParticipantController;
import com.task.lottery.entities.Participant;
import com.task.lottery.services.ParticipantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
@AutoConfigureMockMvc
public class ParticipantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private transient ParticipantService participantService;

    @InjectMocks
    private transient ParticipantController participantController;

    private static Participant p1;

    private ObjectMapper objectMapper;


    @BeforeEach
    void setup() {
        p1 = new Participant(1L, "p", "1", "email1", 10.0);
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.mockMvc = MockMvcBuilders.standaloneSetup(participantController).build();
    }


    /**
     * Test the save end point.
     */
    @Test
    void testSaveParticipant() throws Exception {
        when(this.participantService.save(any(Participant.class))).thenReturn(p1);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/participants/participant")
                        .content(objectMapper.writeValueAsString(p1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Participant participant = objectMapper
                .readValue(result.getResponse().getContentAsString(), Participant.class);

        assertEquals(participant, p1);
        verify(participantService, times(1)).save(any(Participant.class));
    }


    /**
     * Test the find all end point.
     */
    @Test
    void testFindAll() throws Exception {
        Participant p2 = new Participant(2L, "p", "2", "email2", 10.0);
        Participant[] participants = new Participant[] {p1, p2};

        when(this.participantService.findAll()).thenReturn(Arrays.asList(participants));
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/participants/list")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Participant[] res = objectMapper
                .readValue(result.getResponse().getContentAsString(), Participant[].class);

        assertArrayEquals(participants, res);
    }


    /**
     * Test the find by id end point in the case when there is a participant with the given id.
     */
    @Test
    void testFindById() throws Exception {
        when(participantService.findById(1L)).thenReturn(Optional.of(p1));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/participants/participant/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Participant participant = objectMapper
                .readValue(result.getResponse().getContentAsString(), Participant.class);

        assertEquals(participant, p1);

        verify(participantService, times(1)).findById(1L);
    }


    /**
     * Test the find by id end point in the case when there is not a participant with the given id.
     */
    @Test
    void testFindByIdError() throws Exception {
        when(participantService.findById(1L)).thenReturn(Optional.empty());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/participants/participant/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity()).andReturn();
    }
}
