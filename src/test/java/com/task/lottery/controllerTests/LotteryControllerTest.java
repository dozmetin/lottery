package com.task.lottery.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.task.lottery.controllers.LotteryController;
import com.task.lottery.entities.Ballot;
import com.task.lottery.entities.Lottery;
import com.task.lottery.services.LotteryService;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LotteryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private transient LotteryService lotteryService;

    @InjectMocks
    private transient LotteryController lotteryController;

    private static Lottery l1;

    private ObjectMapper objectMapper;



    @BeforeEach
    void setup() {
        l1 = new Lottery(1L, true, LocalDate.now(), 0,
                null, 100.0, 5.0);
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.mockMvc = MockMvcBuilders.standaloneSetup(lotteryController).build();
    }


    /**
     * Test the save end point in the case the service can save the lottery successfully.
     */
    @Test
    void testCreateNewLottery() throws Exception {
        when(this.lotteryService.save(any(Lottery.class))).thenReturn(l1);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/lotteries/lottery")
                        .content(objectMapper.writeValueAsString(l1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Lottery lottery = objectMapper
                .readValue(result.getResponse().getContentAsString(), Lottery.class);

        assertEquals(lottery, l1);
        verify(lotteryService, times(1)).save(any(Lottery.class));
    }


    /**
     * Test the save end point in the case the service can't save the lottery successfully.
     */
    @Test
    void testCreateNewLotteryError() throws Exception {
        when(this.lotteryService.save(any(Lottery.class))).thenThrow(new Exception());
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/lotteries/lottery")
                        .content(objectMapper.writeValueAsString(l1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }


    /**
     * Test the get winner end point in the case when there is a winner on the given date.
     */
    @Test
    void testGetWinner() throws Exception {
        Ballot ballot = new Ballot (1L, 1L, 1L, "123456");
        when(this.lotteryService.getWinner(any(LocalDate.class))).thenReturn(ballot);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/lotteries/lottery/winner")
                        .content(objectMapper.writeValueAsString(LocalDate.now()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Ballot ballotResult = objectMapper
                .readValue(result.getResponse().getContentAsString(), Ballot.class);

        assertEquals(ballot, ballotResult);
        verify(lotteryService, times(1)).getWinner(any(LocalDate.class));
    }


    /**
     * Test the get winner end point in the case when there is not a winner on the given date.
     */
    @Test
    void testGetWinnerError() throws Exception {
        when(this.lotteryService.getWinner(any(LocalDate.class))).thenThrow(new Exception());
        mockMvc.perform(
                MockMvcRequestBuilders.get("/lotteries/lottery/winner")
                        .content(objectMapper.writeValueAsString(LocalDate.now()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isUnprocessableEntity()).andReturn();
    }


    /**
     * Test the find all end point.
     */
    @Test
    void testFindAll() throws Exception {
        Lottery l2 = new Lottery(2L, false, LocalDate.now().plusYears(1), 0,
                null, 100.0, 5.0);
        Lottery[] lotteries = new Lottery[] {l1, l2};

        when(this.lotteryService.findAll()).thenReturn(Arrays.asList(lotteries));
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/lotteries/list")
                        .content(objectMapper.writeValueAsString(LocalDate.now()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Lottery[] res = objectMapper
                .readValue(result.getResponse().getContentAsString(), Lottery[].class);

        assertArrayEquals(lotteries, res);
    }


    /**
     * Test the find by id end point in the case when there is a lottery with the given id.
     */
    @Test
    void testFindById() throws Exception {
        when(lotteryService.findById(1L)).thenReturn(Optional.of(l1));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/lotteries/lottery/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Lottery lottery = objectMapper
                .readValue(result.getResponse().getContentAsString(), Lottery.class);

        assertEquals(lottery, l1);

        verify(lotteryService, times(1)).findById(1L);
    }


    /**
     * Test the find by id end point in the case when there is not a lottery with the given id.
     */
    @Test
    void testFindByIdError() throws Exception {
        when(lotteryService.findById(1L)).thenReturn(Optional.empty());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/lotteries/lottery/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity()).andReturn();
    }

}
