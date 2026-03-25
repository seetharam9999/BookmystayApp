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

    // Getters (Encapsulation)
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

    // Abstract method
    public abstract void displayRoomDetails();
}

// Single Room
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

// Double Room
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

// Suite Room
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

// Main Application
public class HotelApp {
    public static void main(String[] args) {

        // Create room objects (Polymorphism)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability (simple variables)
        int singleAvailability = 5;
        int doubleAvailability = 3;
        int suiteAvailability = 2;

        System.out.println("=== Room Details and Availability ===\n");

        single.displayRoomDetails();
        System.out.println("Available: " + singleAvailability);
        System.out.println();

        doubleRoom.displayRoomDetails();
        System.out.println("Available: " + doubleAvailability);
        System.out.println();

        suite.displayRoomDetails();
        System.out.println("Available: " + suiteAvailability);
    }
}