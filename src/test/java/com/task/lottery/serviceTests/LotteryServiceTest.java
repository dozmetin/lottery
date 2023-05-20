package com.task.lottery.serviceTests;

import com.task.lottery.entities.Ballot;
import com.task.lottery.entities.Lottery;
import com.task.lottery.entities.Participant;
import com.task.lottery.repositories.BallotRepository;
import com.task.lottery.repositories.LotteryRepository;
import com.task.lottery.repositories.ParticipantRepository;
import com.task.lottery.services.LotteryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LotteryServiceTest {

    @Mock
    transient LotteryRepository lotteryRepository;

    @Mock
    transient BallotRepository ballotRepository;

    @Mock
    transient ParticipantRepository participantRepository;

    @InjectMocks
    transient LotteryService lotteryService;

    private static Lottery l1;


    @BeforeEach
    void setup() {
        l1 = new Lottery(1L, true, LocalDate.now(), 0, null, 100.0, 5.0);

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveValidDate() {
        when(lotteryRepository.findByStartTime(l1.getStartTime())).thenReturn(List.of());
        when(lotteryRepository.save(l1)).thenReturn(l1);
        try{
            Lottery saved = lotteryService.save(l1);
            assertNotNull(saved);
            assertTrue(saved.getIsOpen());
        }
        catch (Exception e){
            fail("No exception should be thrown.");
        }
    }


    @Test
    void testSaveInvalidPastDate() {
        LocalDate startDate = LocalDate.now().minusYears(1);
        Lottery l2 = new Lottery(2L, true, startDate, 0, null, 100.0, 10.0);
        assertThrows(IllegalArgumentException.class, () -> {
            lotteryService.save(l2);
        });
        verify(lotteryRepository, never()).save(l2);
    }


    @Test
    void testSaveFutureDate() {
        LocalDate startDate = LocalDate.now().plusYears(1);
        Lottery l2 = new Lottery(2L, true, startDate, 0, null, 100.0, 10.0);
        when(lotteryRepository.findByStartTime(l2.getStartTime())).thenReturn(List.of());
        when(lotteryRepository.save(l2)).thenReturn(l2);
        try{
            Lottery saved = lotteryService.save(l2);
            assertNotNull(saved);
            assertFalse(saved.getIsOpen());
        }
        catch (Exception e){
            fail("No exception should be thrown.");
        }
    }


    @Test
    void testSaveLotteryAlreadyExists() {
        Lottery l2 = new Lottery(2L, true,LocalDate.now(), 0, null, 100.0, 10.0);
        when(lotteryRepository.findByStartTime(l1.getStartTime())).thenReturn(List.of(l2));

        assertThrows(IllegalArgumentException.class, () -> {
            lotteryService.save(l1);
        });

        verify(lotteryRepository, times(1)).findByStartTime(LocalDate.now());
        verify(lotteryRepository, never()).save(l1);
    }


    @Test
    void pickWinner() {
        double oldBalance = 10.0;
        Participant p1 = new Participant(1L, "p", "1", "email1", oldBalance);
        Participant p2 = new Participant(2L, "p", "2", "email2", 100.0);

        Ballot b1 = new Ballot(1L, 1L, 1L, "123456");
        Ballot b2 = new Ballot(1L, 1L, 2L, "463456");
        Ballot b3 = new Ballot(1L, 1L, 2L, "123498");

        List<Ballot> ballots = List.of(b1, b2, b3);

        when(lotteryRepository.findByStartTime(l1.getStartTime())).thenReturn(List.of(l1));
        when(ballotRepository.findByLotteryId(l1.getId())).thenReturn(ballots);
        when(lotteryService.updateFields(l1)).thenReturn(l1);
        when(participantRepository.findById(any(Long.class))).thenReturn(Optional.of(p1));

        lotteryService.pickWinner();
        assertNotNull(l1.getWinnerId());
        assertFalse(l1.getIsOpen());
        assertEquals(p1.getBalance(), (oldBalance + l1.getPrize()));
    }


    @Test
    void testUpdateFields() {
        when(lotteryService.updateFields(l1)).thenReturn(l1);
        assertEquals(l1, lotteryService.updateFields(l1));
    }


    @Test
    void getWinnerValidDate() {
        LocalDate startDate = LocalDate.now().minusYears(1);
        Lottery l2 = new Lottery(2L, false, startDate, 10, 3L, 100.0, 10.0);
        Ballot winnerBallot = new Ballot(3L, 2L, 1L, "123456");
        when(lotteryRepository.findByStartTime(l2.getStartTime())).thenReturn(List.of(l2));
        when(ballotRepository.findById(l2.getWinnerId())).thenReturn(Optional.of(winnerBallot));
        try{
            Ballot winner = lotteryService.getWinner(startDate);
            assertNotNull(winner);
            assertEquals(3L, (long) winner.getId());
        }
        catch (Exception e){
            fail("No exception should be thrown.");
        }
    }


    @Test
    void getWinnerInvalidDate() {
        LocalDate startDate = LocalDate.now().plusYears(1);
        Lottery l2 = new Lottery(2L, false, startDate, 10, 3L, 100.0, 10.0);
        Ballot winnerBallot = new Ballot(3L, 2L, 1L, "123456");
        assertThrows(IllegalArgumentException.class, () -> {
            lotteryService.getWinner(startDate);
        });
        verify(ballotRepository, never()).findById(any(Long.class));
    }


    @Test
    void testStartLotteries() {
        Lottery l2 = new Lottery(2L, false, LocalDate.now(), 10, 3L, 100.0, 10.0);
        when(lotteryRepository.findByStartTime(LocalDate.now())).thenReturn(List.of(l2));
        lotteryService.startLotteries();
        assertTrue(l2.getIsOpen());
    }


    @Test
    void testLotteryExists() {
        when(lotteryRepository.findByStartTime(LocalDate.now())).thenReturn(List.of(l1));
        assertTrue(lotteryService.lotteryExists(LocalDate.now()));
    }

    @Test
    void testLotteryDoesNotExist() {
        when(lotteryRepository.findByStartTime(LocalDate.now())).thenReturn(List.of());
        assertFalse(lotteryService.lotteryExists(LocalDate.now()));
    }


    @Test
    void testFindById() {
        when(lotteryRepository.findById(any(Long.class))).thenReturn(Optional.of(l1));
        Lottery found = lotteryService.findById(1L).get();
        assertEquals(found, l1);
        verify(lotteryRepository, times(1)).findById(1L);
    }

}
