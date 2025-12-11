package com.quickbus;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SeatAllocationEdgeTest {

    @Test
    void allocateUntilFullAndReject() {
        Route r = new Route(20, "EdgeRoute", 60, 3, new String[]{"07:00"});
        assertEquals(3, r.getAvailableSeats());

        int a = r.allocateSeat();
        int b = r.allocateSeat();
        int c = r.allocateSeat();
        assertEquals(0, r.getAvailableSeats());

        // further allocation should return -1 (our implementation)
        int d = r.allocateSeat();
        assertEquals(-1, d);

        // free one seat and allocate again
        r.freeSeat(b);
        assertEquals(1, r.getAvailableSeats());
        int e = r.allocateSeat();
        assertTrue(e >= 1 && e <= 3);
    }
}
