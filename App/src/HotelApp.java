import java.util.*;

// Main Class
public class HotelApp {

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

        public int getId() {
            return id;
        }

        public String getCustomerName() {
            return customerName;
        }

        public String getRoomType() {
            return roomType;
        }

        @Override
        public String toString() {
            return "ID: " + id + ", Name: " + customerName + ", Room: " + roomType;
        }
    }

    // 🔹 BookingHistory Class
    static class BookingHistory {
        private List<Reservation> reservations = new ArrayList<>();

        // Store confirmed booking
        public void addReservation(Reservation r) {
            reservations.add(r); // maintains insertion order
        }

        // Retrieve bookings safely
        public List<Reservation> getAllReservations() {
            return new ArrayList<>(reservations); // return copy
        }
    }

    // 🔹 BookingReportService Class
    static class BookingReportService {

        // Display all bookings
        public void displayAllBookings(List<Reservation> reservations) {
            System.out.println("\n--- Booking History ---");
            for (Reservation r : reservations) {
                System.out.println(r);
            }
        }

        // Summary report
        public void generateSummaryReport(List<Reservation> reservations) {
            System.out.println("\n--- Summary Report ---");
            System.out.println("Total Bookings: " + reservations.size());
        }
    }

    // 🔹 Main Method (Admin Interaction)
    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulate confirmed bookings
        history.addReservation(new Reservation(1, "Alice", "Single"));
        history.addReservation(new Reservation(2, "Bob", "Double"));
        history.addReservation(new Reservation(3, "Charlie", "Suite"));

        // Admin retrieves booking history
        List<Reservation> data = history.getAllReservations();

        // Display stored bookings
        reportService.displayAllBookings(data);

        // Generate report
        reportService.generateSummaryReport(data);
    }
}