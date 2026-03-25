import java.util.*;

// -------------------- Room Domain --------------------
abstract class Room {
    private String type;
    private int beds;
    private double size;
    private double price;

    public Room(String type, int beds, double size, double price) {
        this.type = type;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public String getType() { return type; }
    public int getBeds() { return beds; }
    public double getSize() { return size; }
    public double getPrice() { return price; }

    public abstract void displayRoomDetails();
}

class SingleRoom extends Room {
    public SingleRoom() { super("Single Room", 1, 200, 1000); }
    public void displayRoomDetails() {
        System.out.println(getType() + " | ₹" + getPrice());
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() { super("Double Room", 2, 350, 1800); }
    public void displayRoomDetails() {
        System.out.println(getType() + " | ₹" + getPrice());
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() { super("Suite Room", 3, 600, 3500); }
    public void displayRoomDetails() {
        System.out.println(getType() + " | ₹" + getPrice());
    }
}

// -------------------- Inventory --------------------
class RoomInventory {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoomType(String type, int count) {
        availability.put(type, count);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void decrement(String type) {
        int current = getAvailability(type);
        if (current > 0) {
            availability.put(type, current - 1);
        }
    }

    public void display() {
        System.out.println("\nInventory:");
        for (String key : availability.keySet()) {
            System.out.println(key + " → " + availability.get(key));
        }
    }
}

// -------------------- Reservation --------------------
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

// -------------------- Queue --------------------
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO removal
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// -------------------- Booking Service --------------------
class BookingService {

    // Track ALL allocated room IDs (global uniqueness)
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Track per room type allocations
    private Map<String, Set<String>> roomAllocations = new HashMap<>();

    // Process queue
    public void processBookings(BookingRequestQueue queue, RoomInventory inventory) {

        System.out.println("\n=== Processing Bookings ===");

        while (!queue.isEmpty()) {

            Reservation request = queue.getNextRequest();
            String type = request.getRoomType();

            // Check availability
            if (inventory.getAvailability(type) <= 0) {
                System.out.println("Booking FAILED for " + request.getGuestName() +
                        " (No availability for " + type + ")");
                continue;
            }

            // Generate unique room ID
            String roomId = generateRoomId(type);

            // Ensure uniqueness (Set check)
            if (allocatedRoomIds.contains(roomId)) {
                System.out.println("Duplicate ID detected! Skipping...");
                continue;
            }

            // ---- Atomic Allocation ----
            allocatedRoomIds.add(roomId);

            roomAllocations.putIfAbsent(type, new HashSet<>());
            roomAllocations.get(type).add(roomId);

            inventory.decrement(type);

            // Confirmation
            System.out.println("Booking CONFIRMED for " + request.getGuestName() +
                    " → " + type + " | Room ID: " + roomId);
        }
    }

    // Generate ID (simple unique pattern)
    private String generateRoomId(String type) {
        return type.replace(" ", "").substring(0, 2).toUpperCase()
                + "-" + UUID.randomUUID().toString().substring(0, 5);
    }

    // Display allocations
    public void displayAllocations() {
        System.out.println("\n=== Allocated Rooms ===");
        for (String type : roomAllocations.keySet()) {
            System.out.println(type + " → " + roomAllocations.get(type));
        }
    }
}

// -------------------- Main --------------------
public class HotelApp {
    public static void main(String[] args) {

        // Rooms
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType(single.getType(), 2);
        inventory.addRoomType(doubleRoom.getType(), 1);
        inventory.addRoomType(suite.getType(), 1);

        // Queue (FIFO requests)
        BookingRequestQueue queue = new BookingRequestQueue();
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room")); // exceeds availability
        queue.addRequest(new Reservation("David", "Suite Room"));

        // Booking Service
        BookingService bookingService = new BookingService();

        // Process bookings
        bookingService.processBookings(queue, inventory);

        // Final state
        bookingService.displayAllocations();
        inventory.display();
    }
}