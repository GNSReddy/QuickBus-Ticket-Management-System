package com.quickbus;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.TreeMap;

class BookingSimulationTest {

    @Test
    void simulateSimpleBookingAndCancellation() {
        Route r = new Route(10, "SimRoute", 100, 3, new String[]{"10:00"});
        assertEquals(3, r.getAvailableSeats());

        int seat1 = r.allocateSeat();
        int seat2 = r.allocateSeat();
        assertEquals(1, r.getAvailableSeats());

        Ticket t1 = new Ticket(1001, "User1", r.getId(), r.getName(), seat1, r.getFare());
        Ticket t2 = new Ticket(1002, "User2", r.getId(), r.getName(), seat2, r.getFare());

        Map<Integer, Ticket> tickets = new TreeMap<>();
        tickets.put(t1.getTicketId(), t1);
        tickets.put(t2.getTicketId(), t2);
        assertEquals(2, tickets.size());

        Ticket removed = tickets.remove(1001);
        assertNotNull(removed);
        r.freeSeat(removed.getSeatNumber());
        assertEquals(2, r.getAvailableSeats());
        assertEquals(1, tickets.size());
    }
}
