import java.util.*;

// -------------------- Add-On Service --------------------
class Service {
    private String name;
    private double price;

    public Service(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }

    public void display() {
        System.out.println(name + " (₹" + price + ")");
    }
}

// -------------------- Add-On Service Manager --------------------
class AddOnServiceManager {

    // Map: Reservation ID -> List of Services
    private Map<String, List<Service>> serviceMap = new HashMap<>();

    // Add service to a reservation
    public void addService(String reservationId, Service service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);
    }

    // Get services for a reservation
    public List<Service> getServices(String reservationId) {
        return serviceMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total additional cost
    public double calculateTotalCost(String reservationId) {
        double total = 0;
        for (Service s : getServices(reservationId)) {
            total += s.getPrice();
        }
        return total;
    }

    // Display services
    public void displayServices(String reservationId) {
        System.out.println("\nServices for Reservation: " + reservationId);
        for (Service s : getServices(reservationId)) {
            s.display();
        }
        System.out.println("Total Add-On Cost: ₹" + calculateTotalCost(reservationId));
    }
}

// -------------------- Demo Main --------------------
public class HotelApp {
    public static void main(String[] args) {

        // Assume these reservation IDs came from BookingService
        String res1 = "SR-12345";
        String res2 = "DR-67890";

        // Create services
        Service wifi = new Service("WiFi", 200);
        Service breakfast = new Service("Breakfast", 300);
        Service spa = new Service("Spa", 1000);

        // Service Manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Guest selects services
        manager.addService(res1, wifi);
        manager.addService(res1, breakfast);

        manager.addService(res2, spa);

        // Display services and cost
        manager.displayServices(res1);
        manager.displayServices(res2);
    }
}