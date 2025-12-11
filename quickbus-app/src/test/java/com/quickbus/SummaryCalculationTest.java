package com.quickbus;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.TreeMap;

class SummaryCalculationTest {

    @Test
    void summaryCountsTicketsPerRoute() {
        Route r1 = new Route(1, "R1", 50, 10, new String[]{"08:00"});
        Route r2 = new Route(2, "R2", 70, 10, new String[]{"09:00"});

        // book 2 on r1, 1 on r2
        int s1 = r1.allocateSeat();
        int s2 = r1.allocateSeat();
        int s3 = r2.allocateSeat();

        Ticket t1 = new Ticket(201, "A", r1.getId(), r1.getName(), s1, r1.getFare());
        Ticket t2 = new Ticket(202, "B", r1.getId(), r1.getName(), s2, r1.getFare());
        Ticket t3 = new Ticket(301, "C", r2.getId(), r2.getName(), s3, r2.getFare());

        Map<Integer, Ticket> tickets = new TreeMap<>();
        tickets.put(t1.getTicketId(), t1);
        tickets.put(t2.getTicketId(), t2);
        tickets.put(t3.getTicketId(), t3);

        long soldR1 = tickets.values().stream().filter(t -> t.getRouteId() == r1.getId()).count();
        long soldR2 = tickets.values().stream().filter(t -> t.getRouteId() == r2.getId()).count();
        int total = tickets.size();

        assertEquals(2, soldR1);
        assertEquals(1, soldR2);
        assertEquals(3, total);
    }
}
