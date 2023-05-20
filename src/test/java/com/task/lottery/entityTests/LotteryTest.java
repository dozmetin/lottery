package com.task.lottery.entityTests;

import com.task.lottery.entities.Lottery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class LotteryTest {

    private static Lottery l1;
    private static Lottery l2;

    @BeforeEach
    void setUp() {
        l1 = new Lottery(1L, true, LocalDate.now(), 0,
                null, 100.0, 5.0);
        l2 = new Lottery();
    }


    @Test
    void testConstructor() {
        assertNull(l1.getWinnerId());
        assertEquals(1L, l1.getId());
        assertEquals(100.0, l1.getPrize());
        assertEquals(0, l1.getBallotCount());
        assertTrue(l1.getIsOpen());
        assertEquals(5.0, l1.getBallotPrice());
        assertEquals(LocalDate.now(), l1.getStartTime());
    }

    @Test
    void testEmptyConstructor() {
        assertNotNull(l2);
    }

    @Test
    void testEqualsSameInstance() {
        assertEquals(l1, l1);
        int expectedHashCodeResult = l1.hashCode();
        assertEquals(expectedHashCodeResult, l1.hashCode());
    }


    @Test
    void testEqualsDifferentInstance() {
        assertNotEquals(l1, l2);
        int expectedHashCodeResult = l1.hashCode();
        assertNotEquals(expectedHashCodeResult, l2.hashCode());
    }
}
