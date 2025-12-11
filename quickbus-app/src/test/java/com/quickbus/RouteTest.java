package com.quickbus;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RouteTest {

    @Test
    void allocateAndFreeSeatWorks() {
        Route r = new Route(1, "A-B", 50, 5, new String[]{"08:00"});
        assertEquals(5, r.getAvailableSeats());

        int s1 = r.allocateSeat();
        int s2 = r.allocateSeat();
        assertNotEquals(s1, s2);
        assertEquals(3, r.getAvailableSeats());

        r.freeSeat(s1);
        assertEquals(4, r.getAvailableSeats());
    }

    @Test
    void getBookedSeatsListIsCorrect() {
        Route r = new Route(2, "X-Y", 30, 4, new String[]{"09:00"});
        assertTrue(r.getBookedSeats().isEmpty());
        int seat = r.allocateSeat();
        assertEquals(1, r.getBookedSeats().size());
        assertTrue(r.getBookedSeats().contains(seat));
    }
}

