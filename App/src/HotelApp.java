import java.util.*;

// Main Class
public class HotelApp {

    // 🔹 Custom Exception
    static class InvalidBookingException extends Exception {
        public InvalidBookingException(String message) {
            super(message);
        }
    }

    // 🔹 Reservation Class
    static class Reservation {
        private int id;
        private String customerName;
        private String roomType;

        public Reservation(int id, String customerName, String roomType) {
            this.id = id;
            this.customerName = customerName;
            this.roomType = roomType;
        }

        @Override
        public String toString() {
            return "ID: " + id + ", Name: " + customerName + ", Room: " + roomType;
        }
    }

    // 🔹 Booking System with Validation
    static class BookingSystem {
        private Map<String, Integer> inventory = new HashMap<>();
        private List<Reservation> bookings = new ArrayList<>();

        public BookingSystem() {
            inventory.put("Single", 2);
            inventory.put("Double", 2);
            inventory.put("Suite", 1);
        }

        // Validate and book (Fail-Fast)
        public void bookRoom(int id, String name, String roomType) throws InvalidBookingException {

            // 🔸 Validate room type
            if (!inventory.containsKey(roomType)) {
                throw new InvalidBookingException("Invalid room type: " + roomType);
            }

            // 🔸 Validate availability
            int available = inventory.get(roomType);
            if (available <= 0) {
                throw new InvalidBookingException("No rooms available for type: " + roomType);
            }

            // 🔸 Safe state update
            inventory.put(roomType, available - 1);

            // 🔸 Store booking
            bookings.add(new Reservation(id, name, roomType));

            System.out.println("Booking confirmed for " + name);
        }

        // Display bookings
        public void showBookings() {
            System.out.println("\n--- Confirmed Bookings ---");
            for (Reservation r : bookings) {
                System.out.println(r);
            }
        }
    }

    // 🔹 Main Method
    public static void main(String[] args) {

        BookingSystem system = new BookingSystem();

        try {
            // Valid booking
            system.bookRoom(1, "Alice", "Single");

            // Invalid room type
            system.bookRoom(2, "Bob", "Luxury");

        } catch (InvalidBookingException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            // Exhaust rooms
            system.bookRoom(3, "Charlie", "Suite");
            system.bookRoom(4, "David", "Suite"); // should fail

        } catch (InvalidBookingException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // System continues running safely
        system.showBookings();
    }
}