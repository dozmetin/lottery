package com.task.lottery.serviceTests;

import com.task.lottery.entities.Ballot;
import com.task.lottery.entities.Lottery;
import com.task.lottery.entities.Participant;
import com.task.lottery.repositories.BallotRepository;
import com.task.lottery.services.BallotService;
import com.task.lottery.services.LotteryService;
import com.task.lottery.services.ParticipantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class BallotServiceTest {
    @Mock
    transient LotteryService lotteryService;

    @Mock
    transient BallotRepository ballotRepository;

    @Mock
    transient ParticipantService participantService;

    @InjectMocks
    transient BallotService ballotService;


    private static Ballot b1;

    private static Lottery l1;

    private static Lottery l2;

    private static Participant p1;


    @BeforeEach
    void setup() {
        b1 = new Ballot(1L, 2L, 1L, "123456");
        l1 = new Lottery(2L, true, LocalDate.now(), 0,
                null, 100.0, 5.0);
        l2 = new Lottery(1L, false, LocalDate.now().plusYears(1), 0,
                null, 100.0, 5.0);
        p1 = new Participant(1L, "p", "1", "email1", 10.0);
        MockitoAnnotations.openMocks(this);
    }


    /**
     * Test the save functionality when there is a valid lottery, a valid participant and a valid guess number.
     */
    @Test
    void testSaveValidBallot() {
        when(lotteryService.findById(any(Long.class))).thenReturn(Optional.of(l1));
        when(participantService.findById(any(Long.class))).thenReturn(Optional.of(p1));
        when(lotteryService.updateFields(any(Lottery.class))).thenReturn(l1);
        when(participantService.save(any(Participant.class))).thenReturn(p1);
        when(ballotRepository.save(any(Ballot.class))).thenReturn(b1);
        double oldBalance = p1.getBalance();
        int oldBallotCount = l1.getBallotCount();
        try{
            Ballot saved = ballotService.save(b1);
            assertNotNull(saved);
            assertEquals(1L, (long) saved.getParticipantId());
            assertEquals(2L, (long) saved.getLotteryId());
            assertEquals(oldBalance - l1.getBallotPrice(), p1.getBalance());
            assertEquals(oldBallotCount + l1.getBallotCount(), l1.getBallotCount());
        }
        catch (Exception e){
            fail("No exception should be thrown.");
        }
    }


    @Test
    void testSaveInValidBallotGuess() {
        when(lotteryService.findById(any(Long.class))).thenReturn(Optional.of(l1));
        when(participantService.findById(any(Long.class))).thenReturn(Optional.of(p1));
        when(lotteryService.updateFields(any(Lottery.class))).thenReturn(l1);
        when(participantService.save(any(Participant.class))).thenReturn(p1);
        when(ballotRepository.save(any(Ballot.class))).thenReturn(b1);
        double oldBalance = p1.getBalance();
        int oldBallotCount = l1.getBallotCount();
        Ballot b2 = new Ballot(1L, 2L, 1L, "1234567");
        assertThrows(Exception.class, () -> {
            ballotService.save(b2);
        });
        assertEquals(oldBalance, p1.getBalance());
        assertEquals(oldBallotCount, l1.getBallotCount());
    }


    @Test
    void testSaveClosedLottery() {
        when(lotteryService.findById(any(Long.class))).thenReturn(Optional.of(l2));
        when(participantService.findById(any(Long.class))).thenReturn(Optional.of(p1));
        when(lotteryService.updateFields(any(Lottery.class))).thenReturn(l2);
        when(participantService.save(any(Participant.class))).thenReturn(p1);
        when(ballotRepository.save(any(Ballot.class))).thenReturn(b1);
        double oldBalance = p1.getBalance();
        assertThrows(Exception.class, () -> {
            ballotService.save(b1);
        });

        int oldBallotCount = l1.getBallotCount();
        assertEquals(oldBalance, p1.getBalance());
        assertEquals(oldBallotCount, l1.getBallotCount());
    }


    @Test
    void testSaveNoLottery() {
        when(lotteryService.findById(any(Long.class))).thenReturn(Optional.empty());
        when(participantService.findById(any(Long.class))).thenReturn(Optional.of(p1));
        when(lotteryService.updateFields(any(Lottery.class))).thenReturn(l1);
        when(participantService.save(any(Participant.class))).thenReturn(p1);
        when(ballotRepository.save(any(Ballot.class))).thenReturn(b1);
        double oldBalance = p1.getBalance();
        assertThrows(Exception.class, () -> {
            ballotService.save(b1);
        });

        int oldBallotCount = l1.getBallotCount();
        assertEquals(oldBalance, p1.getBalance());
        assertEquals(oldBallotCount, l1.getBallotCount());
    }


    @Test
    void testSaveNoParticipant() {
        when(lotteryService.findById(any(Long.class))).thenReturn(Optional.of(l1));
        when(participantService.findById(any(Long.class))).thenReturn(Optional.empty());
        when(lotteryService.updateFields(any(Lottery.class))).thenReturn(l1);
        when(participantService.save(any(Participant.class))).thenReturn(p1);
        when(ballotRepository.save(any(Ballot.class))).thenReturn(b1);
        double oldBalance = p1.getBalance();
        assertThrows(Exception.class, () -> {
            ballotService.save(b1);
        });

        int oldBallotCount = l1.getBallotCount();
        assertEquals(oldBalance, p1.getBalance());
        assertEquals(oldBallotCount, l1.getBallotCount());
    }


    @Test
    void testSaveNoParticipantNoLottery() {
        when(lotteryService.findById(any(Long.class))).thenReturn(Optional.empty());
        when(participantService.findById(any(Long.class))).thenReturn(Optional.empty());
        when(lotteryService.updateFields(any(Lottery.class))).thenReturn(l1);
        when(participantService.save(any(Participant.class))).thenReturn(p1);
        when(ballotRepository.save(any(Ballot.class))).thenReturn(b1);
        double oldBalance = p1.getBalance();
        assertThrows(Exception.class, () -> {
            ballotService.save(b1);
        });

        int oldBallotCount = l1.getBallotCount();
        assertEquals(oldBalance, p1.getBalance());
        assertEquals(oldBallotCount, l1.getBallotCount());
    }


    @Test
    void testFindById() {
        when(ballotRepository.findById(any(Long.class))).thenReturn(Optional.of(b1));
        Ballot found = ballotService.findById(1L).get();
        assertEquals(found, b1);
        verify(ballotRepository, times(1)).findById(1L);
    }
}
