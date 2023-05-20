package com.task.lottery.serviceTests;

import com.task.lottery.entities.Participant;
import com.task.lottery.repositories.ParticipantRepository;
import com.task.lottery.services.ParticipantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ParticipantServiceTest {

    @Mock
    transient ParticipantRepository participantRepository;

    @InjectMocks
    transient ParticipantService participantService;

    private static Participant p1;


    @BeforeEach
    void setup() {
        p1 = new Participant(1L, "p", "1", "email1", 10.0);

        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testFindById() {
        when(participantRepository.findById(any(Long.class))).thenReturn(Optional.of(p1));
        Participant found = participantService.findById(1L).get();
        assertEquals(found, p1);
        verify(participantRepository, times(1)).findById(1L);
    }


    @Test
    void findAll() {
        Participant p2 = new Participant(2L, "p", "2", "email2", 10.0);
        Participant p3 = new Participant(3L, "p", "3", "email3", 10.0);
        List<Participant> participants = List.of(p1, p2, p3);
        when(participantRepository.findAll()).thenReturn(participants);
        assertEquals(participants, participantService.findAll());
    }
}
