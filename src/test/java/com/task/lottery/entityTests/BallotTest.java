package com.task.lottery.entityTests;

import com.task.lottery.entities.Ballot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class BallotTest {

    private static Ballot b1;
    private static Ballot b2;

    private static Ballot b3;

    @BeforeEach
    void setUp() {
        b1 = new Ballot(1L, 2L, 1L, "123456");
        b2 = new Ballot(2L, 2L, 1L, "198456");
        b3 = new Ballot();
    }


    @Test
    void testConstructor() {
        assertEquals(1L, b1.getId());
        assertEquals(2L, b1.getLotteryId());
        assertEquals(1L, b1.getParticipantId());
        assertEquals("123456", b1.getGuess());
    }

    @Test
    void testEmptyConstructor() {
        assertNotNull(b3);
    }

    @Test
    void testEqualsSameInstance() {
        assertEquals(b1, b1);
        int expectedHashCodeResult = b1.hashCode();
        assertEquals(expectedHashCodeResult, b1.hashCode());
    }


    @Test
    void testEqualsDifferentInstance() {
        assertNotEquals(b1, b2);
        int expectedHashCodeResult = b1.hashCode();
        assertNotEquals(expectedHashCodeResult, b2.hashCode());
    }

}
