package com.quickbus;

import java.util.*;


public class TicketSystem {

    private static final List<Route> routes = new ArrayList<>();
    private static final Map<Integer, Ticket> tickets = new TreeMap<>();
    private static int ticketCounter = 1000;

    public static void main(String[] args) {
        seedData();
        Scanner sc = new Scanner(System.in);
        int choice;

        System.out.println("=== QuickBus Ticket Management System (v2.0) ===");

        do {
            showMainMenu();
            choice = readInt(sc, "Enter your choice: ");

            switch (choice) {
                case 1 -> showRoutesOnlyNames();
                case 2 -> showRouteDetails(sc);
                case 3 -> buyTicket(sc);
                case 4 -> cancelTicket(sc);
                case 5 -> viewBookings();
                case 6 -> runAdminMenu(sc);
                case 7 -> showSummary();
                case 8 -> System.out.println("Exiting. Thank you for using QuickBus.");
                default -> System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 8);

        sc.close();
    }

    private static void showMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Show Routes");
        System.out.println("2. Show Route Details");
        System.out.println("3. Buy Ticket");
        System.out.println("4. Cancel Ticket");
        System.out.println("5. View All Bookings");
        System.out.println("6. Admin Menu");
        System.out.println("7. Show Summary");
        System.out.println("8. Exit");
    }

    private static void showRoutesOnlyNames() {
        if (routes.isEmpty()) {
            System.out.println("No routes available.");
            return;
        }
        System.out.println("\n--- Available Routes ---");
        for (Route r : routes) {
            System.out.println("ID " + r.getId() + ": " + r.getName());
        }
    }

    private static void showRouteDetails(Scanner sc) {
        showRoutesOnlyNames();
        int id = readInt(sc, "Enter route ID: ");

        Route r = findRouteById(id);
        if (r == null) {
            System.out.println("Route not found.");
            return;
        }

        System.out.println("\n--- Route Details ---");
        System.out.println("Name: " + r.getName());
        System.out.println("Fare: Rs " + r.getFare());
        System.out.println("Total Seats: " + r.getTotalSeats());
        System.out.println("Available Seats: " + r.getAvailableSeats());
        System.out.println("Schedule: " + Arrays.toString(r.getSchedule()));
        List<Integer> booked = r.getBookedSeats();
        if (booked.isEmpty()) {
            System.out.println("No booked seats on this route.");
        } else {
            System.out.println("Booked seats: " + booked);
        }
    }

    private static void buyTicket(Scanner sc) {
        showRoutesOnlyNames();
        int id = readInt(sc, "Enter route ID: ");

        Route r = findRouteById(id);
        if (r == null) {
            System.out.println("Route not found.");
            return;
        }

        if (r.getAvailableSeats() <= 0) {
            System.out.println("No seats available.");
            return;
        }

        sc.nextLine();
        System.out.print("Enter passenger name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Passenger name cannot be empty.");
            return;
        }

        int seat = r.allocateSeat();
        int ticketId = ++ticketCounter;

        Ticket t = new Ticket(ticketId, name, r.getId(), r.getName(), seat, r.getFare());
        tickets.put(ticketId, t);

        System.out.println("\nTicket Booked Successfully!");
        System.out.println(t);
    }

    private static void cancelTicket(Scanner sc) {
        if (tickets.isEmpty()) {
            System.out.println("There are no bookings to cancel.");
            return;
        }

        int id = readInt(sc, "Enter ticket ID: ");

        Ticket t = tickets.remove(id);
        if (t == null) {
            System.out.println("Ticket not found.");
            return;
        }

        Route r = findRouteById(t.getRouteId());
        if (r != null) r.freeSeat(t.getSeatNumber());

        System.out.println("Ticket canceled successfully.");
    }

    private static void viewBookings() {
        if (tickets.isEmpty()) {
            System.out.println("No bookings yet.");
            return;
        }
        System.out.println("\n--- All Bookings ---");
        for (Ticket t : tickets.values()) {
            System.out.println(t);
        }
    }

    // ---------------- Admin Module ----------------
    private static void runAdminMenu(Scanner sc) {
        sc.nextLine();
        System.out.print("Enter admin password: ");
        String pass = sc.nextLine().trim();

        if (!"admin123".equals(pass)) {
            System.out.println("Incorrect password!");
            return;
        }

        int choice;
        do {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add Route");
            System.out.println("2. Remove Route");
            System.out.println("3. Back to Main Menu");
            choice = readInt(sc, "Enter choice: ");

            switch (choice) {
                case 1 -> addRoute(sc);
                case 2 -> removeRoute(sc);
                case 3 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid option.");
            }
        } while (choice != 3);
    }

    private static void addRoute(Scanner sc) {
        sc.nextLine();
        System.out.print("Enter route name (e.g., City A to City B): ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Route name cannot be empty.");
            return;
        }

        int fare = readInt(sc, "Enter fare Rs: ");
        int seats = readInt(sc, "Enter total seats (>=40): ");
        if (seats < 40) {
            System.out.println("Seats must be at least 40. Setting to 40.");
            seats = 40;
        }

        sc.nextLine();
        System.out.print("Enter schedule times separated by commas (e.g., 08:00,12:30): ");
        String scheduleLine = sc.nextLine().trim();
        String[] schedule = scheduleLine.isEmpty() ? new String[0]
                : Arrays.stream(scheduleLine.split(",")).map(String::trim).toArray(String[]::new);

        int newId = routes.stream().mapToInt(Route::getId).max().orElse(0) + 1;
        Route r = new Route(newId, name, fare, seats, schedule);
        routes.add(r);
        System.out.println("Route added with ID " + r.getId());
    }

    private static void removeRoute(Scanner sc) {
        if (routes.isEmpty()) {
            System.out.println("No routes to remove.");
            return;
        }
        System.out.println("\nAvailable routes:");
        for (Route r : routes) {
            System.out.println("ID " + r.getId() + ": " + r.getName());
        }
        int rid = readInt(sc, "Enter route id to remove: ");
        Route r = findRouteById(rid);
        if (r == null) {
            System.out.println("Route not found.");
            return;
        }

        List<Integer> toRemove = new ArrayList<>();
        for (Ticket t : tickets.values()) {
            if (t.getRouteId() == rid) toRemove.add(t.getTicketId());
        }
        for (int id : toRemove) tickets.remove(id);

        routes.remove(r);
        System.out.println("Route removed and " + toRemove.size() + " related tickets removed");
    }

    // ---------------- Summary ----------------
    private static void showSummary() {
        System.out.println("\n--- Ticket Sales Summary ---");
        if (routes.isEmpty()) {
            System.out.println("No routes available.");
            return;
        }

        int totalTickets = tickets.size();
        for (Route r : routes) {
            long sold = tickets.values().stream().filter(t -> t.getRouteId() == r.getId()).count();
            System.out.printf("Route ID %d - %s : Tickets Sold = %d%n", r.getId(), r.getName(), sold);
        }
        System.out.println("TOTAL TICKETS SOLD = " + totalTickets);
    }

    // ---------------- Utilities ----------------
    private static Route findRouteById(int id) {
        for (Route r : routes)
            if (r.getId() == id) return r;
        return null;
    }

    private static int readInt(Scanner sc, String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return sc.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input. Enter a number.");
                sc.nextLine();
            }
        }
    }

    private static void seedData() {
        // seats already >= 40
        routes.add(new Route(1, "City Center to Airport", 120, 40,
                new String[]{"06:00", "09:00", "12:00", "15:00"}));
        routes.add(new Route(2, "Station to Tech Park", 80, 46,
                new String[]{"07:30", "10:30", "14:30"}));
        routes.add(new Route(3, "Market to University", 50, 50,
                new String[]{"08:00", "11:00", "14:00"}));
    }
}

// ---------------- Route Class ----------------
class Route {
    private final int id;
    private final String name;
    private final int fare;
    private final int totalSeats;
    private final String[] schedule;
    private final boolean[] seats;

    public Route(int id, String name, int fare, int totalSeats, String[] schedule) {
        this.id = id;
        this.name = name;
        this.fare = fare;
        this.totalSeats = totalSeats;
        this.schedule = schedule != null ? schedule : new String[0];
        this.seats = new boolean[totalSeats + 1];
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getFare() { return fare; }
    public int getTotalSeats() { return totalSeats; }
    public String[] getSchedule() { return schedule; }

    public int getAvailableSeats() {
        int count = 0;
        for (int i = 1; i <= totalSeats; i++)
            if (!seats[i]) count++;
        return count;
    }

    public int allocateSeat() {
        for (int i = 1; i <= totalSeats; i++)
            if (!seats[i]) {
                seats[i] = true;
                return i;
            }
        return -1;
    }

    public void freeSeat(int seatNo) {
        if (seatNo >= 1 && seatNo <= totalSeats)
            seats[seatNo] = false;
    }

    public List<Integer> getBookedSeats() {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= totalSeats; i++)
            if (seats[i]) list.add(i);
        return list;
    }
}

// ---------------- Ticket Class ----------------
class Ticket {
    private final int ticketId;
    private final String name;
    private final int routeId;
    private final String routeName;
    private final int seatNo;
    private final int fare;

    public Ticket(int ticketId, String name, int routeId, String routeName, int seatNo, int fare) {
        this.ticketId = ticketId;
        this.name = name;
        this.routeId = routeId;
        this.routeName = routeName;
        this.seatNo = seatNo;
        this.fare = fare;
    }

    public int getTicketId() { return ticketId; }
    public String getPassengerName() { return name; }
    public int getRouteId() { return routeId; }
    public int getSeatNumber() { return seatNo; }
    public String getRouteName() { return routeName; }

    @Override
    public String toString() {
        return "TicketID " + ticketId +
                ", Name: " + name +
                ", Route: " + routeName +
                ", Seat: " + seatNo +
                ", Fare: Rs " + fare;
    }
}
