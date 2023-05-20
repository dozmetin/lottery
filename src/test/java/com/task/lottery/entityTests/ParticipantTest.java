package com.task.lottery.entityTests;

import com.task.lottery.entities.Ballot;
import com.task.lottery.entities.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ParticipantTest {

    private static Participant p1;
    private static Participant p2;

    private static Participant p3;

    @BeforeEach
    void setUp() {
        p1 = new Participant(1L, "p", "1", "email1", 10.0);
        p2 = new Participant(2L, "p", "2", "email2", 100.0);
        p3 = new Participant(3L, "p", "3", "email3", 500.0);
    }


    @Test
    void testConstructor() {
        assertEquals(1L, p1.getId());
        assertEquals(10, p1.getBalance());
        assertEquals("p", p1.getFirstName());
        assertEquals("1", p1.getLastName());
        assertEquals("email1", p1.getEmail());
    }

    @Test
    void testEmptyConstructor() {
        assertNotNull(p3);
    }

    @Test
    void testEqualsSameInstance() {
        assertEquals(p1, p1);
        int expectedHashCodeResult = p1.hashCode();
        assertEquals(expectedHashCodeResult, p1.hashCode());
    }


    @Test
    void testEqualsDifferentInstance() {
        assertNotEquals(p1, p2);
        int expectedHashCodeResult = p1.hashCode();
        assertNotEquals(expectedHashCodeResult, p2.hashCode());
    }
}
