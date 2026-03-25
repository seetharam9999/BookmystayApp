import java.io.*;
import java.util.*;

// Main Class
public class HotelApp {

    // 🔹 Reservation Class (Serializable)
    static class Reservation implements Serializable {
        int id;
        String name;
        String roomType;

        public Reservation(int id, String name, String roomType) {
            this.id = id;
            this.name = name;
            this.roomType = roomType;
        }

        @Override
        public String toString() {
            return "ID: " + id + ", Name: " + name + ", Room: " + roomType;
        }
    }

    // 🔹 System State (Serializable)
    static class SystemState implements Serializable {
        Map<String, Integer> inventory;
        List<Reservation> bookings;

        public SystemState(Map<String, Integer> inventory, List<Reservation> bookings) {
            this.inventory = inventory;
            this.bookings = bookings;
        }
    }

    // 🔹 Persistence Service
    static class PersistenceService {

        private static final String FILE_NAME = "hotel_state.dat";

        // Save state
        public void save(SystemState state) {
            try (ObjectOutputStream out =
                         new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

                out.writeObject(state);
                System.out.println("State saved successfully.");

            } catch (IOException e) {
                System.out.println("Error saving state: " + e.getMessage());
            }
        }

        // Load state
        public SystemState load() {
            try (ObjectInputStream in =
                         new ObjectInputStream(new FileInputStream(FILE_NAME))) {

                System.out.println("State loaded successfully.");
                return (SystemState) in.readObject();

            } catch (FileNotFoundException e) {
                System.out.println("No previous data found. Starting fresh.");
            } catch (Exception e) {
                System.out.println("Corrupted data. Starting with safe defaults.");
            }
            return null;
        }
    }

    // 🔹 Booking System
    static class BookingSystem {
        Map<String, Integer> inventory = new HashMap<>();
        List<Reservation> bookings = new ArrayList<>();

        public BookingSystem() {
            inventory.put("Single", 2);
            inventory.put("Double", 2);
        }

        public void book(int id, String name, String roomType) {
            if (!inventory.containsKey(roomType)) {
                System.out.println("Invalid room type");
                return;
            }

            if (inventory.get(roomType) <= 0) {
                System.out.println("No rooms available");
                return;
            }

            inventory.put(roomType, inventory.get(roomType) - 1);
            bookings.add(new Reservation(id, name, roomType));

            System.out.println("Booking confirmed for " + name);
        }

        public void showData() {
            System.out.println("\n--- Bookings ---");
            for (Reservation r : bookings) {
                System.out.println(r);
            }
            System.out.println("Inventory: " + inventory);
        }

        // Restore state
        public void restore(SystemState state) {
            if (state != null) {
                this.inventory = state.inventory;
                this.bookings = state.bookings;
            }
        }

        // Get snapshot
        public SystemState getState() {
            return new SystemState(inventory, bookings);
        }
    }

    // 🔹 Main Method
    public static void main(String[] args) {

        PersistenceService persistence = new PersistenceService();
        BookingSystem system = new BookingSystem();

        // 🔸 Load previous state
        SystemState savedState = persistence.load();
        system.restore(savedState);

        // 🔸 Continue operations
        system.book(1, "Alice", "Single");
        system.book(2, "Bob", "Double");

        system.showData();

        // 🔸 Save state before shutdown
        persistence.save(system.getState());
    }
}