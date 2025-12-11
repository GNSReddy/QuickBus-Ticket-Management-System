package com.quickbus;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TicketTest {

    @Test
    void ticketGettersReturnCorrectValues() {
        Ticket t = new Ticket(101, "Alice", 5, "CityA to CityB", 12, 75);
        assertEquals(101, t.getTicketId());
        assertEquals("Alice", t.getPassengerName());
        assertEquals(5, t.getRouteId());
        assertEquals(12, t.getSeatNumber());
        assertEquals("CityA to CityB", t.getRouteName());
    }

    @Test
    void ticketToStringContainsImportantInfo() {
        Ticket t = new Ticket(102, "Bob", 6, "R1", 3, 40);
        String repr = t.toString();

        // check the key pieces separately (less brittle than checking one exact substring)
        assertTrue(repr.contains("TicketID 102"), "toString must include ticket id");
        assertTrue(repr.contains("Bob"), "toString must include passenger name");
        assertTrue(repr.contains("Seat"), "toString must mention seat");
        assertTrue(repr.contains("3") || repr.contains("Seat 3"),
                   "toString must include the seat number (3)");
    }
}
