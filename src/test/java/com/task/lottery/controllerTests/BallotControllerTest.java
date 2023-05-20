package com.task.lottery.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.task.lottery.controllers.BallotController;
import com.task.lottery.entities.Ballot;
import com.task.lottery.services.BallotService;
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
public class BallotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private transient BallotService ballotService;

    @InjectMocks
    private transient BallotController ballotController;

    private static Ballot b1;

    private ObjectMapper objectMapper;


    @BeforeEach
    void setup() {
        b1 = new Ballot(1L, 2L, 1L, "123456");
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.mockMvc = MockMvcBuilders.standaloneSetup(ballotController).build();
    }


    /**
     * Test the save end point in the case there are no errors and the service is able to save successfully.
     */
    @Test
    void testCreateNewBallot() throws Exception {
        when(this.ballotService.save(any(Ballot.class))).thenReturn(b1);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/ballots/ballot")
                        .content(objectMapper.writeValueAsString(b1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Ballot ballot = objectMapper
                .readValue(result.getResponse().getContentAsString(), Ballot.class);

        assertEquals(ballot, b1);
        verify(ballotService, times(1)).save(any(Ballot.class));
    }


    /**
     * Test the case when the service throws an exception during saving.
     */
    @Test
    void testCreateNewBallotError() throws Exception {
        when(this.ballotService.save(any(Ballot.class))).thenThrow(new IllegalArgumentException());
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/ballots/ballot")
                        .content(objectMapper.writeValueAsString(b1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }


    /**
     * Test the find all end point.
     */
    @Test
    void testFindAll() throws Exception {
        Ballot b2 = new Ballot(2L, 2L, 1L, "198456");
        Ballot[] ballots = new Ballot[] {b1, b2};

        when(this.ballotService.findAll()).thenReturn(Arrays.asList(ballots));
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/ballots/list")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Ballot[] res = objectMapper
                .readValue(result.getResponse().getContentAsString(), Ballot[].class);

        assertArrayEquals(ballots, res);
    }


    /**
     * Test the find by id end point in the case when there is a ballot with the given id.
     */
    @Test
    void testFindById() throws Exception {
        when(ballotService.findById(1L)).thenReturn(Optional.of(b1));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/ballots/ballot/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Ballot ballot = objectMapper
                .readValue(result.getResponse().getContentAsString(), Ballot.class);

        assertEquals(ballot, b1);

        verify(ballotService, times(1)).findById(1L);
    }


    /**
     * Test the find by id end point in the case when there is not a ballot with the given id.
     */
    @Test
    void testFindByIdError() throws Exception {
        when(ballotService.findById(1L)).thenReturn(Optional.empty());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/ballots/ballot/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity()).andReturn();
    }
}
