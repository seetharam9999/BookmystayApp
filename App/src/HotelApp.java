import java.util.HashMap;
import java.util.Map;

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
        System.out.println("Type: " + getType());
        System.out.println("Beds: " + getBeds());
        System.out.println("Size: " + getSize() + " sq.ft");
        System.out.println("Price: ₹" + getPrice());
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 350.0, 1800.0);
    }

    @Override
    public void displayRoomDetails() {
        System.out.println("Type: " + getType());
        System.out.println("Beds: " + getBeds());
        System.out.println("Size: " + getSize() + " sq.ft");
        System.out.println("Price: ₹" + getPrice());
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 600.0, 3500.0);
    }

    @Override
    public void displayRoomDetails() {
        System.out.println("Type: " + getType());
        System.out.println("Beds: " + getBeds());
        System.out.println("Size: " + getSize() + " sq.ft");
        System.out.println("Price: ₹" + getPrice());
    }
}

// RoomInventory Class (Centralized State Management)
class RoomInventory {
    private Map<String, Integer> availability;

    // Constructor initializes inventory
    public RoomInventory() {
        availability = new HashMap<>();
    }

    // Register room type with count
    public void addRoomType(String roomType, int count) {
        availability.put(roomType, count);
    }

    // Get availability
    public int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    // Update availability (controlled modification)
    public void updateAvailability(String roomType, int newCount) {
        if (availability.containsKey(roomType)) {
            availability.put(roomType, newCount);
        } else {
            System.out.println("Room type not found: " + roomType);
        }
    }

    // Display full inventory
    public void displayInventory() {
        System.out.println("\n=== Current Room Inventory ===");
        for (Map.Entry<String, Integer> entry : availability.entrySet()) {
            System.out.println(entry.getKey() + " → Available: " + entry.getValue());
        }
    }
}

// Main Application
public class HotelApp {
    public static void main(String[] args) {

        // Create room objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Register room types
        inventory.addRoomType(single.getType(), 5);
        inventory.addRoomType(doubleRoom.getType(), 3);
        inventory.addRoomType(suite.getType(), 2);

        // Display room details with availability
        System.out.println("=== Room Details and Availability ===\n");

        single.displayRoomDetails();
        System.out.println("Available: " + inventory.getAvailability(single.getType()));
        System.out.println();

        doubleRoom.displayRoomDetails();
        System.out.println("Available: " + inventory.getAvailability(doubleRoom.getType()));
        System.out.println();

        suite.displayRoomDetails();
        System.out.println("Available: " + inventory.getAvailability(suite.getType()));

        // Demonstrate update
        System.out.println("\nUpdating availability...\n");
        inventory.updateAvailability("Single Room", 4);

        // Display centralized inventory
        inventory.displayInventory();
    }
}