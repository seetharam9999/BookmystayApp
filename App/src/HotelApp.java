import java.util.*;

// Main Class
public class HotelApp {

    // 🔹 Custom Exception
    static class BookingException extends Exception {
        public BookingException(String message) {
            super(message);
        }
    }

    // 🔹 Reservation Class
    static class Reservation {
        int id;
        String name;
        String roomType;
        String roomId; // allocated room ID
        boolean active;

        public Reservation(int id, String name, String roomType, String roomId) {
            this.id = id;
            this.name = name;
            this.roomType = roomType;
            this.roomId = roomId;
            this.active = true;
        }

        @Override
        public String toString() {
            return "ID: " + id + ", Name: " + name + ", Room: " + roomType +
                    ", RoomID: " + roomId + ", Status: " + (active ? "Active" : "Cancelled");
        }
    }

    // 🔹 Booking + Cancellation System
    static class BookingSystem {

        private Map<String, Integer> inventory = new HashMap<>();
        private Map<Integer, Reservation> reservations = new HashMap<>();

        // 🔸 Stack for rollback (LIFO)
        private Stack<String> rollbackStack = new Stack<>();

        public BookingSystem() {
            inventory.put("Single", 2);
            inventory.put("Double", 2);
            inventory.put("Suite", 1);
        }

        // 🔹 Booking (for setup)
        public void bookRoom(int id, String name, String roomType) throws BookingException {

            if (!inventory.containsKey(roomType)) {
                throw new BookingException("Invalid room type");
            }

            if (inventory.get(roomType) <= 0) {
                throw new BookingException("No rooms available");
            }

            // Allocate room ID
            String roomId = roomType.substring(0, 1) + (inventory.get(roomType));

            inventory.put(roomType, inventory.get(roomType) - 1);

            Reservation r = new Reservation(id, name, roomType, roomId);
            reservations.put(id, r);

            System.out.println("Booking confirmed: " + r);
        }

        // 🔹 Cancellation with rollback
        public void cancelBooking(int id) throws BookingException {

            // 🔸 Validate existence
            if (!reservations.containsKey(id)) {
                throw new BookingException("Reservation does not exist");
            }

            Reservation r = reservations.get(id);

            // 🔸 Prevent duplicate cancellation
            if (!r.active) {
                throw new BookingException("Booking already cancelled");
            }

            // 🔸 Step 1: Record for rollback (LIFO)
            rollbackStack.push(r.roomId);

            // 🔸 Step 2: Restore inventory
            inventory.put(r.roomType, inventory.get(r.roomType) + 1);

            // 🔸 Step 3: Update booking status
            r.active = false;

            System.out.println("Booking cancelled: ID " + id +
                    ", Released RoomID: " + r.roomId);
        }

        // 🔹 Display all bookings
        public void showBookings() {
            System.out.println("\n--- Booking History ---");
            for (Reservation r : reservations.values()) {
                System.out.println(r);
            }
        }

        // 🔹 Show rollback stack
        public void showRollbackStack() {
            System.out.println("\nRollback Stack (Recent Releases): " + rollbackStack);
        }
    }

    // 🔹 Main Method
    public static void main(String[] args) {

        BookingSystem system = new BookingSystem();

        try {
            // Bookings
            system.bookRoom(1, "Alice", "Single");
            system.bookRoom(2, "Bob", "Double");

            // Valid cancellation
            system.cancelBooking(1);

            // Invalid cancellation (already cancelled)
            system.cancelBooking(1);

        } catch (BookingException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            // Invalid cancellation (non-existent)
            system.cancelBooking(99);

        } catch (BookingException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // System remains consistent
        system.showBookings();
        system.showRollbackStack();
    }
}