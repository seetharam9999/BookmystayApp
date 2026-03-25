import java.util.*;

// Abstract Room Class
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

    public String getType() {
        return type;
    }

    public int getBeds() {
        return beds;
    }

    public double getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public abstract void displayRoomDetails();
}

// Concrete Room Classes
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 200.0, 1000.0);
    }

    @Override
    public void displayRoomDetails() {
        System.out.println(getType() + " | Beds: " + getBeds() + " | Price: ₹" + getPrice());
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 350.0, 1800.0);
    }

    @Override
    public void displayRoomDetails() {
        System.out.println(getType() + " | Beds: " + getBeds() + " | Price: ₹" + getPrice());
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 600.0, 3500.0);
    }

    @Override
    public void displayRoomDetails() {
        System.out.println(getType() + " | Beds: " + getBeds() + " | Price: ₹" + getPrice());
    }
}

// Inventory (unchanged, no mutation in this stage)
class RoomInventory {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoomType(String type, int count) {
        availability.put(type, count);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }
}

// Reservation (Booking Request)
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Guest: " + guestName + " requested " + roomType);
    }
}

// Booking Request Queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add request (enqueue)
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added to queue:");
        reservation.display();
    }

    // View all queued requests (no removal)
    public void viewQueue() {
        System.out.println("\n=== Booking Request Queue ===");
        for (Reservation r : queue) {
            r.display();
        }
    }

    // Peek next request (FIFO, no removal)
    public Reservation peekNext() {
        return queue.peek();
    }
}

// Main Application
public class HotelApp {
    public static void main(String[] args) {

        // Room setup
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Inventory setup (no changes later)
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType(single.getType(), 5);
        inventory.addRoomType(doubleRoom.getType(), 3);
        inventory.addRoomType(suite.getType(), 2);

        // Booking Queue
        BookingRequestQueue requestQueue = new BookingRequestQueue();

        // Guests submit booking requests
        requestQueue.addRequest(new Reservation("Alice", "Single Room"));
        requestQueue.addRequest(new Reservation("Bob", "Suite Room"));
        requestQueue.addRequest(new Reservation("Charlie", "Double Room"));

        // View queue (arrival order preserved)
        requestQueue.viewQueue();

        // Peek next request (FIFO behavior)
        System.out.println("\nNext request to process:");
        Reservation next = requestQueue.peekNext();
        if (next != null) {
            next.display();
        }
    }
}