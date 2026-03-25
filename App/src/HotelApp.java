import java.util.*;

// Main Class
public class HotelApp {

    // 🔹 Booking Request
    static class BookingRequest {
        int id;
        String name;
        String roomType;

        public BookingRequest(int id, String name, String roomType) {
            this.id = id;
            this.name = name;
            this.roomType = roomType;
        }
    }

    // 🔹 Shared Booking System
    static class BookingSystem {

        private Map<String, Integer> inventory = new HashMap<>();

        public BookingSystem() {
            inventory.put("Single", 2);
            inventory.put("Double", 1);
        }

        // 🔹 Critical Section (Thread-Safe)
        public synchronized void processBooking(BookingRequest request) {

            System.out.println(Thread.currentThread().getName() +
                    " processing booking for " + request.name);

            // Check availability
            if (!inventory.containsKey(request.roomType)) {
                System.out.println("Invalid room type for " + request.name);
                return;
            }

            int available = inventory.get(request.roomType);

            if (available > 0) {
                // Simulate delay (to expose race condition if not synchronized)
                try { Thread.sleep(100); } catch (InterruptedException e) {}

                inventory.put(request.roomType, available - 1);

                System.out.println("Booking CONFIRMED for " + request.name +
                        " | Room: " + request.roomType);
            } else {
                System.out.println("Booking FAILED for " + request.name +
                        " | No rooms available");
            }
        }

        public void showInventory() {
            System.out.println("\nFinal Inventory: " + inventory);
        }
    }

    // 🔹 Worker Thread
    static class BookingProcessor extends Thread {
        private Queue<BookingRequest> queue;
        private BookingSystem system;

        public BookingProcessor(Queue<BookingRequest> queue, BookingSystem system) {
            this.queue = queue;
            this.system = system;
        }

        @Override
        public void run() {
            while (true) {
                BookingRequest request;

                // 🔸 Synchronized queue access
                synchronized (queue) {
                    if (queue.isEmpty()) break;
                    request = queue.poll();
                }

                // 🔸 Process booking (critical section inside method)
                system.processBooking(request);
            }
        }
    }

    // 🔹 Main Method
    public static void main(String[] args) {

        BookingSystem system = new BookingSystem();

        // Shared booking queue
        Queue<BookingRequest> queue = new LinkedList<>();

        // Simulate multiple guest requests
        queue.add(new BookingRequest(1, "Alice", "Single"));
        queue.add(new BookingRequest(2, "Bob", "Single"));
        queue.add(new BookingRequest(3, "Charlie", "Single")); // extra request
        queue.add(new BookingRequest(4, "David", "Double"));
        queue.add(new BookingRequest(5, "Eve", "Double")); // extra request

        // Multiple threads (guests)
        Thread t1 = new BookingProcessor(queue, system);
        Thread t2 = new BookingProcessor(queue, system);
        Thread t3 = new BookingProcessor(queue, system);

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {}

        // Final state
        system.showInventory();
    }
}